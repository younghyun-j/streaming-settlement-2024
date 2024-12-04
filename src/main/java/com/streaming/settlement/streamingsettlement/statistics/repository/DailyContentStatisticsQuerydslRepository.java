package com.streaming.settlement.streamingsettlement.statistics.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeStatisticsDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.QCumulativeStatisticsDto;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.streaming.settlement.streamingsettlement.statistics.entity.QDailyContentStatistics.dailyContentStatistics;


@Repository
@RequiredArgsConstructor
public class DailyContentStatisticsQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<DailyContentStatistics> findDailyContentStatisticsByCondition(Long cursorId, LocalDate watchedDate, Long fetchSize) {
        return jpaQueryFactory
                .select(dailyContentStatistics)
                .from(dailyContentStatistics)
                .where(
                        watchedDateEq(watchedDate),
                        cursorCondition(cursorId)
                )
                .orderBy(dailyContentStatistics.id.asc())
                .limit(fetchSize)
                .fetch();

    }

    public List<CumulativeStatisticsDto> findCumulativeContentStatisticsByContentId(List<Long> contentIds) {
            return jpaQueryFactory
                    .select(
                        new QCumulativeStatisticsDto(
                                dailyContentStatistics.id,
                                dailyContentStatistics.contentId,
                                dailyContentStatistics.totalContentViews,
                                dailyContentStatistics.totalAdViews,
                                dailyContentStatistics.totalPlaybackTime
                        )
                    )
                    .from(dailyContentStatistics)
                    .where(
                            dailyContentStatistics.contentId.in(contentIds)
                            .and(dailyContentStatistics.watchedDate.eq(
                                    JPAExpressions
                                            .select(dailyContentStatistics.watchedDate.max())
                                            .from(dailyContentStatistics)
                                            .where(dailyContentStatistics.contentId.eq(dailyContentStatistics.contentId))
                            ))
                    )
                    .fetch();
    }

    public Long findMinIdByWatchedDate(LocalDate watchedDate) {
        return jpaQueryFactory
                .select(dailyContentStatistics.id.min())
                .from(dailyContentStatistics)
                .where(watchedDateEq(watchedDate))
                .fetchOne();
    }

    public Long findMaxIdByWatchedDate(LocalDate watchedDate) {
        return jpaQueryFactory
                .select(dailyContentStatistics.id.max())
                .from(dailyContentStatistics)
                .where(watchedDateEq(watchedDate))
                .fetchOne();
    }

    private static BooleanExpression watchedDateEq(LocalDate date) {
        return dailyContentStatistics.watchedDate.eq(date);
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId == null ? null : dailyContentStatistics.id.goe(cursorId);
    }
}
