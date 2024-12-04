package com.streaming.settlement.streamingsettlement.statistics.repository;

import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyContentStatisticsRepository extends JpaRepository<DailyContentStatistics, Long>, DailyContentStatisticsCustomRepository {

}
