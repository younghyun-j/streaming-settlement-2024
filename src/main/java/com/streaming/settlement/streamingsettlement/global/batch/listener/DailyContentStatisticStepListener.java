package com.streaming.settlement.streamingsettlement.global.batch.listener;

import com.streaming.settlement.streamingsettlement.streaming.repository.DailyWatchedContentHistoryQuerydslRepository;
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
public class DailyContentStatisticStepListener implements StepExecutionListener {

    private final DailyWatchedContentHistoryQuerydslRepository dailyWatchedContentHistoryQuerydslRepository;

    @Value("#{jobParameters['date']}")
    private LocalDate date;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long start = dailyWatchedContentHistoryQuerydslRepository.findMinIdByWatchedDate(date);
        Long end = dailyWatchedContentHistoryQuerydslRepository.findMaxIdByWatchedDate(date);

        log.info("startContentId = {}", start);
        log.info("endContentId = {}", end);

        stepExecution.getExecutionContext().put("startContentId", start);
        stepExecution.getExecutionContext().put("endContentId", end);
    }
}
