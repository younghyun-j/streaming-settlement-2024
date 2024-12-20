package com.streaming.settlement.streamingsettlement.global.batch.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record UserContentWatchHistoryDto(
        Long id,
        Long userId,
        Long contentId,
        Long lastPlaybackPosition,
        Long totalPlaybackTime,
        LocalDate watchedDate
) {
    @QueryProjection
    public UserContentWatchHistoryDto {
    }
}
