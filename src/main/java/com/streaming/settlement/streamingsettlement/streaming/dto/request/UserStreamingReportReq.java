package com.streaming.settlement.streamingsettlement.streaming.dto.request;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record UserStreamingReportReq(
        @NotBlank(message = "마지막 재생 시점은 필수 값입니다.")
        Long lastPlaybackPosition,
        @NotBlank(message = "총 재생 시간은 필수 값입니다.")
        Long totalPlaybackTime,
        @NotBlank(message = "재생 시작 날짜는 필수 값입니다.")
        LocalDate playbackStartDate,
        List<Long> advertiseIds

) {
    public UserContentWatchHistory toUserContentStreamingLog(Long userId, Long contentId) {
        return new UserContentWatchHistory(userId, contentId, lastPlaybackPosition, totalPlaybackTime, playbackStartDate);
    }

    public List<UserAdWatchHistory> toUserAdStreamingLogs(Long userId, Long contentId) {
        return advertiseIds.stream()
                .map(id -> new UserAdWatchHistory(userId, contentId, id, playbackStartDate))
                .toList();
    }
}
