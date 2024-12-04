package com.streaming.settlement.streamingsettlement.global.batch.dto;

import com.querydsl.core.annotations.QueryProjection;

public record CumulativeSettlementDto(
        Long id,
        Long contentId,
        Long totalContentRevenue,
        Long totalAdRevenue) {
    @QueryProjection
    public CumulativeSettlementDto {
    }
}
