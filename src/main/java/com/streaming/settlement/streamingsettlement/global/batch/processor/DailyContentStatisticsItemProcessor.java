package com.streaming.settlement.streamingsettlement.global.batch.processor;

import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeStatisticsDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.UserAdWatchHistoryDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.UserContentWatchHistoryDto;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import com.streaming.settlement.streamingsettlement.streaming.repository.UserAdWatchHistoryQuerydslRepository;
import com.streaming.settlement.streamingsettlement.streaming.repository.UserContentWatchHistoryQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DailyContentStatisticsItemProcessor implements ItemProcessor<CumulativeStatisticsDto, DailyContentStatistics> {

    @Value("#{jobParameters['date']}")
    private LocalDate date;
    private final Long fetchSize = 1000L;
    private final UserContentWatchHistoryQuerydslRepository contentWatchHistoryQuerydslRepository;
    private final UserAdWatchHistoryQuerydslRepository userAdWatchHistoryQuerydslRepository;

    @Override
    public DailyContentStatistics process(CumulativeStatisticsDto cumulativeStatisticsDto) throws Exception {
        DailyContentStatistics dailyContentStatistics = DailyContentStatistics.builder()
                .contentId(cumulativeStatisticsDto.contentId())
                .contentViews(0L)
                .adViews(0L)
                .totalContentViews(cumulativeStatisticsDto.totalContentViews())
                .totalAdViews(cumulativeStatisticsDto.totalAdViews())
                .totalPlaybackTime(cumulativeStatisticsDto.totalPlaybackTime())
                .watchedDate(date)
                .build();

        aggregationDailyContent(dailyContentStatistics);
        aggregationDailyAd(dailyContentStatistics);

        return dailyContentStatistics;
    }

    /**
     * 일일 영상 조회수, 재생시간 & 누적 영상 조회수, 재생시간 집계
     */
    private void aggregationDailyContent(DailyContentStatistics dailyContentStatistics) {
        Long contentCursorId = null;

        while(true) {
            long startTime = System.currentTimeMillis();
            List<UserContentWatchHistoryDto> contentWatchHistories = fetchDailyContentHistories(dailyContentStatistics, contentCursorId);

            dailyContentStatistics.updateContentStatistics(contentWatchHistories);

            if(contentWatchHistories.isEmpty() || contentWatchHistories.size() < fetchSize) {
                break;
            }

            contentCursorId = contentWatchHistories.getLast().id();
        }
    }

    /**
     * 필요한 데이터만 가져오기
     */
    private List<UserContentWatchHistoryDto> fetchDailyContentHistories(DailyContentStatistics dailyContentStatistics, Long cursorId) {
        return contentWatchHistoryQuerydslRepository
                .findWatchHistoriesByContentId(dailyContentStatistics.getContentId(), cursorId, date, fetchSize);
    }

    /**
     * 일일 광고 조회수, 누적 광고 조회수 집계
     */
    private void aggregationDailyAd(DailyContentStatistics dailyContentStatistics) {
        Long adCursorId = null;

        while(true) {
            List<UserAdWatchHistoryDto> adWatchHistories = fetchDailyAdHistories(dailyContentStatistics, adCursorId);
            dailyContentStatistics.updateAdStatistics(adWatchHistories);

            if(adWatchHistories.isEmpty() || adWatchHistories.size() < fetchSize) {
                break;
            }

            adCursorId = adWatchHistories.getLast().id();
        }
    }

    private List<UserAdWatchHistoryDto> fetchDailyAdHistories(DailyContentStatistics dailyContentStatistics, Long cursorId) {
        return userAdWatchHistoryQuerydslRepository
                .findUserAdWatchHistoriesByCondition(dailyContentStatistics.getContentId(), cursorId, date, fetchSize);
    }


}

