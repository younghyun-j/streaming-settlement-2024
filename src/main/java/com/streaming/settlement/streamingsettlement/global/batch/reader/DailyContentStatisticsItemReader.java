package com.streaming.settlement.streamingsettlement.global.batch.reader;

import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeStatisticsDto;
import com.streaming.settlement.streamingsettlement.statistics.repository.DailyContentStatisticsQuerydslRepository;
import com.streaming.settlement.streamingsettlement.streaming.repository.DailyWatchedContentHistoryQuerydslRepository;
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
public class DailyContentStatisticsItemReader implements ItemReader<CumulativeStatisticsDto> {
    private final DailyContentStatisticsQuerydslRepository dailyContentStatisticsQuerydslRepository;
    private final DailyWatchedContentHistoryQuerydslRepository dailyWatchedContentHistoryQuerydslRepository;
    private final Queue<CumulativeStatisticsDto> cumulativeStatisticsQueue = new LinkedList<>();

    @Value("${spring.batch.chunk.size}")
    private Long chunkSize;
    @Value("#{jobParameters['date']}")
    private LocalDate date;
    @Value("#{stepExecutionContext['startContentId']}")
    private Long startContentId;
    @Value("#{stepExecutionContext['endContentId']}")
    private Long endContentId;
    private Long currentStartId;

    @PostConstruct
    public void init() {
        log.info("startContentId: {}", startContentId);
        log.info("endContentId: {}", endContentId);
        this.currentStartId = startContentId;
    }

    @Override
    public CumulativeStatisticsDto read() throws Exception {
        if (cumulativeStatisticsQueue.isEmpty() && hasNextChunk()) {
            processNextChunk();
        }
        return cumulativeStatisticsQueue.poll();
    }

    private boolean hasNextChunk() {
        return currentStartId <= endContentId;
    }

    private void processNextChunk() {
        long fetchSize = Math.min(chunkSize, endContentId - currentStartId + 1);
        fetchAndFillCumulativeContentStatisticsQueue(fetchSize);
        currentStartId += fetchSize;
    }

    private void fetchAndFillCumulativeContentStatisticsQueue(long fetchSize) {
        List<Long> contentIds = fetchDailyViewedContentIds(fetchSize);
        List<CumulativeStatisticsDto> statistics = fetchCumulativeContentStatistics(contentIds);
        cumulativeStatisticsQueue.addAll(statistics);
    }

    /**
     * 일일 스트리밍이 발생한 영상 아이디 조회
     */
    private List<Long> fetchDailyViewedContentIds(long fetchSize) {
        return dailyWatchedContentHistoryQuerydslRepository.findContentIdsByWatchedStartDate(date, currentStartId, fetchSize);
    }

    /**
     * 일일 스트리밍이 발생한 영상에 대한 누적 데이터 조회
     */
    private List<CumulativeStatisticsDto> fetchCumulativeContentStatistics(List<Long> contentIds) {
        List<CumulativeStatisticsDto> statistics = dailyContentStatisticsQuerydslRepository.findCumulativeContentStatisticsByContentId(contentIds);
        if (statistics.size() < contentIds.size()) {
            return fillCumulativeStatisticsDto(contentIds, statistics);
        }
        return statistics;
    }

    /**
     * 일일 통계 기록이 없는 영상의 경우 초기값 객체 생성 후 리스트 반환
     */
    private static List<CumulativeStatisticsDto> fillCumulativeStatisticsDto(List<Long> contentIds, List<CumulativeStatisticsDto> statistics) {
        Map<Long, CumulativeStatisticsDto> statisticsMap = statistics.stream()
                .collect(Collectors.toMap(
                        CumulativeStatisticsDto::contentId,
                        stat -> stat
                ));

        return contentIds.stream()
                .map(contentId -> statisticsMap.getOrDefault(
                        contentId,
                        new CumulativeStatisticsDto(
                                null,       // id
                                contentId,  // contentId
                                0L,        // totalContentViews
                                0L,        // totalAdViews
                                0L         // totalPlaybackTime
                        )
                ))
                .toList();
    }

}