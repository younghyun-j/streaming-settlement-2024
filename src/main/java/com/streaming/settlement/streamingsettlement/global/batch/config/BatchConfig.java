package com.streaming.settlement.streamingsettlement.global.batch.config;

import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeStatisticsDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.DailyStatisticsAndCumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.global.batch.incrementer.CustomJobParameterIncrementer;
import com.streaming.settlement.streamingsettlement.global.batch.partition.DailyContentSettlementPartitioner;
import com.streaming.settlement.streamingsettlement.global.batch.partition.DailyContentStatisticsPartitioner;
import com.streaming.settlement.streamingsettlement.global.batch.processor.DailyContentSettlementItemProcessor;
import com.streaming.settlement.streamingsettlement.global.batch.processor.DailyContentStatisticsItemProcessor;
import com.streaming.settlement.streamingsettlement.global.batch.reader.DailyContentSettlementItemReader;
import com.streaming.settlement.streamingsettlement.global.batch.reader.DailyContentStatisticsItemReader;
import com.streaming.settlement.streamingsettlement.global.batch.writer.DailyContentSettlementItemWriter;
import com.streaming.settlement.streamingsettlement.global.batch.writer.DailyContentStatisticsItemWriter;
import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Value("${spring.batch.chunk.size}")
    private int CHUNK_SIZE;
    @Value("${spring.batch.pool.size}")
    private int poolSize;

    private static final String JOB_NAME = "daily-statistics-settlement-job";
    private static final String STATISTICS_MASTER_STEP_NAME = "daily-statistics-master-step";
    private static final String STATISTICS_PARTITION_STEP_NAME = "daily-statistics-partition-step";
    private static final String STATISTICS_STEP_NAME = "daily-statistics-step";
    private static final String SETTLEMENT_MASTER_STEP_NAME = "daily-settlement-master-step";
    private static final String SETTLEMENT_PARTITION_STEP_NAME = "daily-settlement-partition-step";
    private static final String SETTLEMENT_STEP_NAME = "daily-settlement-step";

    @Bean
    public Job dailyStatisticsSettlementJob(
            JobRepository jobRepository,
            CustomJobParameterIncrementer jobParameterIncrementer,
            Step dailyStatisticsPartitionMasterStep,
            Step dailySettlementPartitionMasterStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(jobParameterIncrementer)
                .start(dailyStatisticsPartitionMasterStep)
                .next(dailySettlementPartitionMasterStep)
                .build();
    }

    @Bean
    public Step dailyStatisticsPartitionMasterStep(
            JobRepository jobRepository,
            DailyContentStatisticsPartitioner dailyContentStatisticsPartitioner,
            TaskExecutorPartitionHandler dailyStatisticsPartitionHandler) {
        return new StepBuilder(STATISTICS_MASTER_STEP_NAME, jobRepository)
                .partitioner(STATISTICS_PARTITION_STEP_NAME, dailyContentStatisticsPartitioner)
                .partitionHandler(dailyStatisticsPartitionHandler)
                .build();
    }

    @Bean
    public Step dailySettlementPartitionMasterStep(
            JobRepository jobRepository,
            DailyContentSettlementPartitioner dailyContentSettlementPartitioner,
            TaskExecutorPartitionHandler dailySettlementPartitionHandler) {
        return new StepBuilder(SETTLEMENT_MASTER_STEP_NAME, jobRepository)
                .partitioner(SETTLEMENT_PARTITION_STEP_NAME, dailyContentSettlementPartitioner)
                .partitionHandler(dailySettlementPartitionHandler)
                .build();
    }

    @Bean
    public TaskExecutorPartitionHandler dailyStatisticsPartitionHandler(Step dailyStatisticsStep) {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setStep(dailyStatisticsStep);
        partitionHandler.setTaskExecutor(executor());
        partitionHandler.setGridSize(poolSize);
        return partitionHandler;
    }

    @Bean
    public TaskExecutorPartitionHandler dailySettlementPartitionHandler(Step dailySettlementStep) {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setStep(dailySettlementStep);
        partitionHandler.setTaskExecutor(executor());
        partitionHandler.setGridSize(poolSize);
        return partitionHandler;
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("statistics-settlement-thread");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    @Bean
    public Step dailyStatisticsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            DailyContentStatisticsItemReader dailyContentStatisticsItemReader,
            DailyContentStatisticsItemProcessor dailyContentStatisticsItemProcessor,
            DailyContentStatisticsItemWriter dailyContentStatisticsItemWriter
            ) {
        return new StepBuilder(STATISTICS_STEP_NAME, jobRepository)
                .<CumulativeStatisticsDto, DailyContentStatistics>chunk(CHUNK_SIZE, transactionManager)
                .reader(dailyContentStatisticsItemReader)
                .processor(dailyContentStatisticsItemProcessor)
                .writer(dailyContentStatisticsItemWriter)
                .build();
    }

    @Bean
    public Step dailySettlementStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            DailyContentSettlementItemReader dailyContentSettlementItemReader,
            DailyContentSettlementItemProcessor dailyContentSettlementItemProcessor,
            DailyContentSettlementItemWriter dailyContentSettlementItemWriter
            ) {
        return new StepBuilder(SETTLEMENT_STEP_NAME, jobRepository)
                .<DailyStatisticsAndCumulativeSettlementDto, DailyContentSettlement>chunk(CHUNK_SIZE, transactionManager)
                .reader(dailyContentSettlementItemReader)
                .processor(dailyContentSettlementItemProcessor)
                .writer(dailyContentSettlementItemWriter)
                .build();
    }

}
