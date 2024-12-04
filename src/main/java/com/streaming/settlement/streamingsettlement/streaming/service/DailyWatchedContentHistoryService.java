package com.streaming.settlement.streamingsettlement.streaming.service;

import java.time.LocalDate;

public interface DailyWatchedContentHistoryService {
    void addDailyStreamingContentId(Long contentId, LocalDate playbackStartDate);
}
