package com.streaming.settlement.streamingsettlement.global.redis.dto;

public record AbusingKey(
        Long userId,
        Long contentId,
        Long creatorId,
        String ip
) {
}
