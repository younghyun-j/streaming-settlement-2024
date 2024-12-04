package com.streaming.settlement.streamingsettlement.global.batch.dto;

import com.querydsl.core.annotations.QueryProjection;

public record CumulativeStatisticsDto(
        Long id,
        Long contentId,
        Long totalContentViews,
        Long totalAdViews,
        Long totalPlaybackTime) {
    @QueryProjection
    public CumulativeStatisticsDto {
    }
}
