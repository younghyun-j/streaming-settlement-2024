package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserContentWatchHistoryRepository extends JpaRepository<UserContentWatchHistory, Long> {
    Optional<UserContentWatchHistory> findFirstByUserIdAndContentIdOrderByWatchedDateDesc(Long userId, Long contentId);
}
