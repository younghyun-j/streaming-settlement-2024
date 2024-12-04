package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;

import java.util.List;

public interface UserAdWatchHistoryService {
    void create(List<UserAdWatchHistory> userAdvertisementViewedHistories);
}
