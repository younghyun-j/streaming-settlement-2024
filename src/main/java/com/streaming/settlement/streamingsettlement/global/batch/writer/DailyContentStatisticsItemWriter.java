package com.streaming.settlement.streamingsettlement.global.batch.writer;

import com.streaming.settlement.streamingsettlement.statistics.repository.DailyContentStatisticsRepository;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DailyContentStatisticsItemWriter implements ItemWriter<DailyContentStatistics> {

    private final DailyContentStatisticsRepository dailyStatisticsRepository;

    @Override
    public void write(Chunk<? extends DailyContentStatistics> chunk) throws Exception {
        List<DailyContentStatistics> dailyContentStatistics = chunk.getItems().stream()
                        .map(item -> (DailyContentStatistics) item)
                        .toList();
        dailyStatisticsRepository.bulkInsertDailyStatistic(dailyContentStatistics);
    }
}
