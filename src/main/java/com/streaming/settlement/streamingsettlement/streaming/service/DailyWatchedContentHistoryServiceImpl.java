package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.global.redis.service.DailyStreamingContentCacheService;
import com.streaming.settlement.streamingsettlement.streaming.entity.DailyWatchedContentHistory;
import com.streaming.settlement.streamingsettlement.streaming.repository.DailyWatchedContentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyWatchedContentHistoryServiceImpl implements DailyWatchedContentHistoryService {

    private final DailyWatchedContentHistoryRepository dailyWatchedContentHistoryRepository;
    private final DailyStreamingContentCacheService dailyStreamingContentCacheService;

    @Transactional
    @Override
    public void addDailyStreamingContentId(Long contentId, LocalDate playbackStartDate) {
        Boolean existContentId = dailyStreamingContentCacheService.isExistContentId(contentId);
        if(!existContentId) {
            dailyWatchedContentHistoryRepository.save(new DailyWatchedContentHistory(contentId, playbackStartDate));
            dailyStreamingContentCacheService.setContentId(contentId);
        }
    }
}
