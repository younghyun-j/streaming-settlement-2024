package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;

public interface UserContentWatchHistoryService {
    void create(UserContentWatchHistory userContentWatchHistory);
    Long getLastPlaybackPositionOfLatestStreaming(Long userId, Long contentId);
}
