package com.streaming.settlement.streamingsettlement.global.batch.dto;

import com.querydsl.core.annotations.QueryProjection;

public record DailyContentAggregationDto(Long totalViews, Long totalPlaybackTime) {
    @QueryProjection
    public DailyContentAggregationDto {
    }
}
