package com.streaming.settlement.streamingsettlement.streaming.service;


import com.streaming.settlement.streamingsettlement.content.entity.Content;
import com.streaming.settlement.streamingsettlement.content.service.ContentService;
import com.streaming.settlement.streamingsettlement.streaming.dto.response.StreamingContentRes;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreamingServiceImpl implements StreamingService {

    private final ContentService contentService;
    private final UserContentWatchHistoryService userContentWatchHistoryService;
    private final UserAdWatchHistoryService userAdWatchHistoryService;

    @Override
    public StreamingContentRes getStreamingContent(Long userId, Long contentId) {
        Content getContent = contentService.getContent(contentId);
        Long lastPlaybackPosition = userContentWatchHistoryService.getLastPlaybackPositionOfLatestStreaming(userId, getContent.getId());
        return StreamingContentRes.of(getContent, lastPlaybackPosition);
    }

    @Transactional
    @Override
    public void endStreamingContent(UserContentWatchHistory streamingLog, List<UserAdWatchHistory> userAdvertisementViewedHistories) {
        userContentWatchHistoryService.create(streamingLog);
        userAdWatchHistoryService.create(userAdvertisementViewedHistories);
    }
}
