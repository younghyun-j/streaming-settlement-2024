package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.streaming.settlement.streamingsettlement.streaming.entity.QUserContentWatchHistory.userContentWatchHistory;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserContentWatchHistoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<UserContentWatchHistory> findWatchHistoriesByContentId(Long contentId, Long cursorId, LocalDate date, Long fetchSize) {
        return jpaQueryFactory
                .select(userContentWatchHistory)
                .from(userContentWatchHistory)
                .where(
                        userContentWatchHistory.contentId.eq(contentId),
                        userContentWatchHistory.watchedDate.eq(date),
                        cursorCondition(cursorId)
                )
                .limit(fetchSize)
                .fetch();
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId == null ? null : userContentWatchHistory.id.gt(cursorId);
    }
}