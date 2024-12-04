package com.streaming.settlement.streamingsettlement.global.batch.listener;

import com.streaming.settlement.streamingsettlement.statistics.repository.DailyContentStatisticsQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DailyContentSettlementStepListener implements StepExecutionListener {

    private final DailyContentStatisticsQuerydslRepository dailyContentStatisticsQuerydslRepository;

    @Value("#{jobParameters['date']}")
    private LocalDate date;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long start = dailyContentStatisticsQuerydslRepository.findMinIdByWatchedDate(date);
        Long end = dailyContentStatisticsQuerydslRepository.findMaxIdByWatchedDate(date);

        log.info("startStatisticsId = {}", start);
        log.info("endStatisticsId = {}", end);

        stepExecution.getExecutionContext().put("startStatisticsId", start);
        stepExecution.getExecutionContext().put("endStatisticsId", end);
    }
}
