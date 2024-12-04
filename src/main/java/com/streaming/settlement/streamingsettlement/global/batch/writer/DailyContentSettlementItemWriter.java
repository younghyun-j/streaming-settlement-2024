package com.streaming.settlement.streamingsettlement.global.batch.writer;

import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;
import com.streaming.settlement.streamingsettlement.settlement.repository.DailyContentSettlementRepository;
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
public class DailyContentSettlementItemWriter implements ItemWriter<DailyContentSettlement> {

    private final DailyContentSettlementRepository dailyContentSettlementRepository;

    @Override
    public void write(Chunk<? extends DailyContentSettlement> chunk) throws Exception {
        List<DailyContentSettlement> dailyContentSettlements = chunk.getItems().stream()
                        .map(item -> (DailyContentSettlement) item)
                        .toList();
        dailyContentSettlementRepository.bulkInsertDailySettlement(dailyContentSettlements);
    }
}
