package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.streaming.settlement.streamingsettlement.streaming.entity.QUserAdWatchHistory.userAdWatchHistory;

@Repository
@RequiredArgsConstructor
public class UserAdWatchHistoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<UserAdWatchHistory> findUserAdWatchHistoriesByCondition(Long contentId, Long cursorId, LocalDate date, Long fetchSize) {
        return jpaQueryFactory
                .select(userAdWatchHistory)
                .from(userAdWatchHistory)
                .where(
                        userAdWatchHistory.contentId.eq(contentId),
                        userAdWatchHistory.watchedDate.eq(date),
                        cursorCondition(cursorId)
                )
                .limit(fetchSize)
                .fetch();

    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId == null ? null : userAdWatchHistory.id.gt(cursorId);
    }
}
