package com.streaming.settlement.streamingsettlement.statistics.repository;

import com.streaming.settlement.streamingsettlement.statistics.entity.DailyContentStatistics;
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
public class DailyContentStatisticsCustomRepositoryImpl implements DailyContentStatisticsCustomRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    @Override
    public void bulkInsertDailyStatistic(List<DailyContentStatistics> dailyStatistics) {

        String sql = """
                INSERT daily_content_statistics(content_id, content_views, ad_views, playback_time, total_content_views, total_ad_views, total_playback_time, watched_date)
                VALUES (:contentId, :contentViews, :adViews, :playbackTime, :totalContentViews, :totalAdViews, :totalPlaybackTime, :watchedDate)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getInsertDailyContentStatisticToSqlParameterSources(dailyStatistics));
    }

    private MapSqlParameterSource[] getInsertDailyContentStatisticToSqlParameterSources(List<DailyContentStatistics> dailyStatistics) {
        return dailyStatistics.stream()
                .map(this::getInsertDailyContentStatisticToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getInsertDailyContentStatisticToSqlParameterSource(DailyContentStatistics dailyContentStatistics) {
        return new MapSqlParameterSource()
                .addValue("contentId", dailyContentStatistics.getContentId())
                .addValue("contentViews", dailyContentStatistics.getTotalContentViews())
                .addValue("adViews", dailyContentStatistics.getAdViews())
                .addValue("playbackTime", dailyContentStatistics.getPlaybackTime())
                .addValue("totalContentViews", dailyContentStatistics.getContentViews())
                .addValue("totalAdViews", dailyContentStatistics.getTotalAdViews())
                .addValue("totalPlaybackTime", dailyContentStatistics.getTotalPlaybackTime())
                .addValue("watchedDate", dailyContentStatistics.getWatchedDate());
    }
}
