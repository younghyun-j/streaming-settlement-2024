package com.streaming.settlement.streamingsettlement.global.batch.partition;

import com.streaming.settlement.streamingsettlement.statistics.repository.DailyContentStatisticsQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class DailyContentSettlementPartitioner implements Partitioner {

    @Value("#{jobParameters['date']}")
    private LocalDate date;
    private final DailyContentStatisticsQuerydslRepository dailyContentStatisticsQuerydslRepository;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long minId = dailyContentStatisticsQuerydslRepository.findMinIdByWatchedDate(date);
        long maxId = dailyContentStatisticsQuerydslRepository.findMaxIdByWatchedDate(date);
        long partitionSize = (maxId - minId) / gridSize + 1;

        return createPartitions(minId, maxId, partitionSize);
    }

    private Map<String, ExecutionContext> createPartitions(long minId, long maxId, long partitionSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        int partitionNumber = 1;
        long currentPartitionStartId = minId;
        long currentPartitionEndId = currentPartitionStartId + partitionSize - 1;

        while (currentPartitionStartId <= maxId) {
            currentPartitionEndId = Math.min(currentPartitionEndId, maxId);

            ExecutionContext context = new ExecutionContext();
            context.putLong("startStatisticsId", currentPartitionStartId);
            context.putLong("endStatisticsId", currentPartitionEndId);
            partitions.put(String.format("partition%d", partitionNumber), context);

            currentPartitionStartId += partitionSize;
            currentPartitionEndId += partitionSize;
            partitionNumber++;
        }

        return partitions;
    }

}
