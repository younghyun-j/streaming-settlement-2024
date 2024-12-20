package com.streaming.settlement.streamingsettlement.global.batch.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record UserAdWatchHistoryDto(
        Long id,
        Long userId,
        Long contentId,
        Long adId,
        LocalDate watchedDate
) {
    @QueryProjection
    public UserAdWatchHistoryDto {
    }
}
