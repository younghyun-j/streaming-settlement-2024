package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdWatchHistoryRepository extends JpaRepository<UserAdWatchHistory, Long>, UserAdWatchHistoryCustomRepository {

}
