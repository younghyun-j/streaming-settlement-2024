package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.streaming.settlement.streamingsettlement.streaming.entity.QDailyWatchedContentHistory.dailyWatchedContentHistory;


@Repository
@RequiredArgsConstructor
public class DailyWatchedContentHistoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Long> findContentIdsByWatchedStartDate(LocalDate streamingStartDate, Long cursorId, Long size) {
        return jpaQueryFactory
                .select(dailyWatchedContentHistory.contentId)
                .from(dailyWatchedContentHistory)
                .where(
                    watchedDateEq(streamingStartDate),
                    dailyWatchedContentHistory.id.goe(cursorId)
                )
                .orderBy(dailyWatchedContentHistory.id.asc())
                .limit(size)
                .fetch();
    }

    public Long findMinIdByWatchedDate(LocalDate watchedDate) {
        return jpaQueryFactory
                .select(dailyWatchedContentHistory.contentId.min())
                .from(dailyWatchedContentHistory)
                .where(watchedDateEq(watchedDate))
                .fetchOne();
    }

    public Long findMaxIdByWatchedDate(LocalDate watchedDate) {
        return jpaQueryFactory
                .select(dailyWatchedContentHistory.contentId.max())
                .from(dailyWatchedContentHistory)
                .where(watchedDateEq(watchedDate))
                .fetchOne();
    }

    private static BooleanExpression watchedDateEq(LocalDate date) {
        return dailyWatchedContentHistory.watchedDate.eq(date);
    }


}
