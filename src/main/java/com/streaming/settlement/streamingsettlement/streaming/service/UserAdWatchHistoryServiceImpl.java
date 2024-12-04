package com.streaming.settlement.streamingsettlement.streaming.service;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.repository.UserAdWatchHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdWatchHistoryServiceImpl implements UserAdWatchHistoryService {

    private final UserAdWatchHistoryRepository userAdStreamingLogRepository;

    @Transactional
    @Override
    public void create(List<UserAdWatchHistory> userAdvertisementViewedHistories) {
        userAdStreamingLogRepository.saveAll(userAdvertisementViewedHistories);
    }
}
