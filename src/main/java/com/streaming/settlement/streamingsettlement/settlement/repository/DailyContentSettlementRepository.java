package com.streaming.settlement.streamingsettlement.settlement.repository;


import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyContentSettlementRepository extends JpaRepository<DailyContentSettlement, Long>, DailyContentSettlementCustomRepository {
}
