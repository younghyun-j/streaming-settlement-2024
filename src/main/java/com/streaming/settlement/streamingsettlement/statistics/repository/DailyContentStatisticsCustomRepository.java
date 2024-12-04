package com.streaming.settlement.streamingsettlement.statistics.repository;

import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;

import java.util.List;

public interface DailyContentStatisticsCustomRepository {
    void bulkInsertDailyStatistic(List<DailyContentStatistics> cumulativeStatistics);
}
