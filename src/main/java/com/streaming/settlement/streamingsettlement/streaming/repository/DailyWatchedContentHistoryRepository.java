package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.streaming.settlement.streamingsettlement.streaming.entity.DailyWatchedContentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyWatchedContentHistoryRepository extends JpaRepository<DailyWatchedContentHistory, Long> {
}
