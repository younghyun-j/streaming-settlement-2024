package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;

import java.util.List;

public interface UserAdWatchHistoryCustomRepository {
    void saveAll(List<UserAdWatchHistory> logs);
}
