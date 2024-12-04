package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.repository.UserContentWatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserContentWatchHistoryServiceImpl implements UserContentWatchHistoryService {

    private final UserContentWatchHistoryRepository userContentWatchHistoryRepository;
    private static final Long INITIAL_PLAYBACK_POSITION = 0L;

    @Transactional
    @Override
    public void create(UserContentWatchHistory userContentWatchHistory) {
        logValidation(userContentWatchHistory);
        userContentWatchHistoryRepository.save(userContentWatchHistory);
    }

    @Override
    public Long getLastPlaybackPositionOfLatestStreaming(Long userId, Long contentId) {
        return userContentWatchHistoryRepository.findFirstByUserIdAndContentIdOrderByWatchedDateDesc(userId, contentId)
                .map(UserContentWatchHistory::getLastPlaybackPosition)
                .orElse(INITIAL_PLAYBACK_POSITION);
    }

    private static void logValidation(UserContentWatchHistory userContentWatchHistory) {
        if (!userContentWatchHistory.isPlaybackStartDateBeyondCurrent()) {
            // TODO : 서비스 예외 처리
            throw new RuntimeException("Invalid playback start date");
        }
        if (userContentWatchHistory.isTotalPlaybackTimeExceedsLastPlay()) {
            // TODO : 서비스 예외 처리
            throw new RuntimeException("Total playback time exceeds last playback time");
        }
    }

}
