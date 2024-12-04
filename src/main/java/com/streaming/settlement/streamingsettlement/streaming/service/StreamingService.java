package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.streaming.dto.response.StreamingContentRes;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;

import java.util.List;

public interface StreamingService {
    StreamingContentRes getStreamingContent(Long userId, Long contentId);
    void endStreamingContent(UserContentWatchHistory streamingLog, List<UserAdWatchHistory> userAdvertisementViewedHistories);
}
