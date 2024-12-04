package com.streaming.settlement.streamingsettlement.settlement.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.QCumulativeSettlementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.streaming.settlement.streamingsettlement.settlement.entity.QDailyContentSettlement.dailyContentSettlement;


@Repository
@RequiredArgsConstructor
public class DailyContentSettlementQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CumulativeSettlementDto> findCumulativeContentSettlementsByContentIds(List<Long> contentIds) {
        return jpaQueryFactory
                .select(
                        new QCumulativeSettlementDto(
                                dailyContentSettlement.id,
                                dailyContentSettlement.contentId,
                                dailyContentSettlement.totalContentRevenue,
                                dailyContentSettlement.totalAdRevenue
                        )
                )
                .from(dailyContentSettlement)
                .where(
                        dailyContentSettlement.contentId.in(contentIds)
                        .and(dailyContentSettlement.watchedDate.eq(
                                JPAExpressions
                                        .select(dailyContentSettlement.watchedDate.max())
                                        .from(dailyContentSettlement)
                                        .where(dailyContentSettlement.contentId.eq(dailyContentSettlement.contentId))
                        ))
                )
                .fetch();
    }
}
