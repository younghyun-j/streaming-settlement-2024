package com.streaming.settlement.streamingsettlement.global.batch.reader;

import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.DailyStatisticsAndCumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.statistics.repository.DailyContentStatisticsQuerydslRepository;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import com.streaming.settlement.streamingsettlement.settlement.repository.DailyContentSettlementQuerydslRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;


@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DailyContentSettlementItemReader implements ItemReader<DailyStatisticsAndCumulativeSettlementDto> {
    private final DailyContentStatisticsQuerydslRepository dailyContentStatisticsQuerydslRepository;
    private final DailyContentSettlementQuerydslRepository dailyContentSettlementQuerydslRepository;
    private final Queue<DailyStatisticsAndCumulativeSettlementDto> statisticsQueue = new LinkedList<>();

    @Value("${spring.batch.chunk.size}")
    private Long chunkSize;
    @Value("#{jobParameters['date']}")
    private LocalDate date;
    @Value("#{stepExecutionContext['startStatisticsId']}")
    private Long startStatisticId;
    @Value("#{stepExecutionContext['endStatisticsId']}")
    private Long endStatisticId;
    private Long currentStartId;

    @PostConstruct
    public void init() {
        log.info("startStatisticId = {}", startStatisticId);
        log.info("endStatisticId = {}", endStatisticId);
        this.currentStartId = startStatisticId;
    }

    @Override
    public DailyStatisticsAndCumulativeSettlementDto read() throws Exception {
        if (statisticsQueue.isEmpty() && hasNextChunk()) {
            processNextChunk();
        }
        return statisticsQueue.poll();
    }

    private boolean hasNextChunk() {
        return currentStartId <= endStatisticId;
    }

    private void processNextChunk() {
        long fetchSize = Math.min(chunkSize, endStatisticId - currentStartId + 1);
        fetchAndFillContentStatisticsQueue(fetchSize);
        currentStartId += fetchSize;
    }

    private void fetchAndFillContentStatisticsQueue(long fetchSize) {
        List<DailyContentStatistics> statistics = fetchDailyContentStatistics(fetchSize);
        List<Long> contentIds = statistics.stream().map(DailyContentStatistics::getContentId).toList();
        List<CumulativeSettlementDto> cumulativeSettlementDtos = fetchCumulativeContentSettlements(contentIds);
        List<DailyStatisticsAndCumulativeSettlementDto> dailyStatisticsAndCumulativeSettlementDtos = createDailyStatisticsAndCumulativeSettlementDtos(cumulativeSettlementDtos, statistics);

        statisticsQueue.addAll(dailyStatisticsAndCumulativeSettlementDtos);
    }

    /**
     * 영상 아이디에 따른 일일 통계과 누적 정산 테이터 매칭 및
     * 일일 정산 기록이 없는 영상의 경우 초기값 객체 생성 후 리스트 반환
     */
    private static List<DailyStatisticsAndCumulativeSettlementDto> createDailyStatisticsAndCumulativeSettlementDtos(List<CumulativeSettlementDto> cumulativeSettlementDtos, List<DailyContentStatistics> statistics) {
        Map<Long, CumulativeSettlementDto> cumulativeSettlementDtoMap = cumulativeSettlementDtos.stream()
                .collect(Collectors.toMap(
                        CumulativeSettlementDto::contentId,
                        settlementDto -> settlementDto
                ));

        return statistics.stream()
                .map(dailyStat -> new DailyStatisticsAndCumulativeSettlementDto(
                        dailyStat,
                        cumulativeSettlementDtoMap.getOrDefault(
                                dailyStat.getContentId(),
                                new CumulativeSettlementDto(
                                        null,       // id
                                        dailyStat.getContentId(), // contentId
                                        0L, // totalContentRevenue
                                        0L // totalAdRevenue
                                )
                )))
                .toList();
    }

    /**
     * 일일 스트리밍이 발생한 영상에 대한 통계 데이터 조회
     */
    private List<DailyContentStatistics> fetchDailyContentStatistics(long fetchSize) {
        return dailyContentStatisticsQuerydslRepository.findDailyContentStatisticsByCondition(currentStartId, date, fetchSize);
    }

    /**
     * 일일 스트리밍이 발생한 영상에 대한 누적 정산 데이터 조회
     */
    private List<CumulativeSettlementDto> fetchCumulativeContentSettlements(List<Long> contentIds) {
        return dailyContentSettlementQuerydslRepository.findCumulativeContentSettlementsByContentIds(contentIds);
    }

}