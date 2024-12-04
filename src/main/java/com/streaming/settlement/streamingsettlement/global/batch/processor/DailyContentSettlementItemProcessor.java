package com.streaming.settlement.streamingsettlement.global.batch.processor;


import com.streaming.settlement.streamingsettlement.global.batch.dto.CumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.DailyStatisticsAndCumulativeSettlementDto;
import com.streaming.settlement.streamingsettlement.settlement.domain.AdRevenueRange;
import com.streaming.settlement.streamingsettlement.settlement.domain.ContentRevenueRange;
import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;
import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DailyContentSettlementItemProcessor implements ItemProcessor<DailyStatisticsAndCumulativeSettlementDto, DailyContentSettlement> {

    @Value("#{jobParameters['date']}")
    private LocalDate date;

    @Override
    public DailyContentSettlement process(DailyStatisticsAndCumulativeSettlementDto dailyStatisticsAndCumulativeSettlementDto) throws Exception {
        DailyContentStatistics dailyContentStatistics = dailyStatisticsAndCumulativeSettlementDto.dailyContentStatistics();
        CumulativeSettlementDto cumulativeSettlementDto = dailyStatisticsAndCumulativeSettlementDto.cumulativeSettlementDto();

        // 영상 누적 정산, 일일 정산
        long totalContentRevenue = ContentRevenueRange.calculateRevenueByViews(dailyContentStatistics.getTotalContentViews());
        long dailyContentRevenue = totalContentRevenue - cumulativeSettlementDto.totalContentRevenue();

        // 광고 누적 정산, 일일 정산
        long totalAdRevenue = AdRevenueRange.calculateRevenueByViews(dailyContentStatistics.getTotalAdViews());
        long dailyAdRevenue = totalAdRevenue - cumulativeSettlementDto.totalAdRevenue();

        return DailyContentSettlement.builder()
                .contentId(cumulativeSettlementDto.contentId())
                .contentRevenue(dailyContentRevenue)
                .adRevenue(dailyAdRevenue)
                .totalContentRevenue(totalContentRevenue)
                .totalAdRevenue(totalAdRevenue)
                .watchedDate(date)
                .build();
    }
}

