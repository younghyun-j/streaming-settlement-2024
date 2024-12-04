package com.streaming.settlement.streamingsettlement.streaming.dto.response;

import com.streaming.settlement.streamingsettlement.content.entity.Content;

import java.time.LocalDate;

public record StreamingContentRes(
    Long contentId,
    Long creatorId,
    String contentUrl,
    Long startPlaybackPosition,
    LocalDate playbackStartDate
) {
    public static StreamingContentRes of(Content content, Long playBackPosition) {
        return new StreamingContentRes(content.getId(), content.getCreatorId(), content.getUrl(), playBackPosition, LocalDate.now());
    }
}
