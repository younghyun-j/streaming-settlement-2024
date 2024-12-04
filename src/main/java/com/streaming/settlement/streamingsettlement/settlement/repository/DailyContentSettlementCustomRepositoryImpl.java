package com.streaming.settlement.streamingsettlement.settlement.repository;

import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DailyContentSettlementCustomRepositoryImpl implements DailyContentSettlementCustomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    @Override
    public void bulkInsertDailySettlement(List<DailyContentSettlement> dailyContentSettlements) {
        String sql = """
                INSERT daily_content_settlement(content_id, content_revenue, ad_revenue, total_content_revenue, total_ad_revenue, watched_date)
                VALUES (:contentId, :contentRevenue, :adRevenue, :totalContentRevenue, :totalAdRevenue, :watchedDate)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getInsertDailyContentStatisticToSqlParameterSources(dailyContentSettlements));
    }

    private MapSqlParameterSource[] getInsertDailyContentStatisticToSqlParameterSources(List<DailyContentSettlement> dailyContentSettlements) {
        return dailyContentSettlements.stream()
                .map(this::getInsertDailyContentStatisticToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getInsertDailyContentStatisticToSqlParameterSource(DailyContentSettlement dailyContentSettlements) {
        return new MapSqlParameterSource()
                .addValue("contentId", dailyContentSettlements.getContentId())
                .addValue("contentRevenue", dailyContentSettlements.getContentRevenue())
                .addValue("adRevenue", dailyContentSettlements.getAdRevenue())
                .addValue("totalContentRevenue", dailyContentSettlements.getTotalContentRevenue())
                .addValue("totalAdRevenue", dailyContentSettlements.getTotalAdRevenue())
                .addValue("watchedDate", dailyContentSettlements.getWatchedDate());
    }

}
