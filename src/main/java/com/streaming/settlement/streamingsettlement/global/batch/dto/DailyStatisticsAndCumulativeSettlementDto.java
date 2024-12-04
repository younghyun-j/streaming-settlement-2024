package com.streaming.settlement.streamingsettlement.global.batch.dto;


import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;

public record DailyStatisticsAndCumulativeSettlementDto(
        DailyContentStatistics dailyContentStatistics,
        CumulativeSettlementDto cumulativeSettlementDto
) {
}
