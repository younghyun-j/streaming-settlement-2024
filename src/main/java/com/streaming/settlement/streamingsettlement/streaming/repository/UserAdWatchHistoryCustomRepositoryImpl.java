package com.streaming.settlement.streamingsettlement.streaming.repository;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserAdWatchHistoryCustomRepositoryImpl implements UserAdWatchHistoryCustomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    @Override
    public void saveAll(List<UserAdWatchHistory> logs) {
        String sql = """
                    INSERT INTO user_advertisement_streaming_log (user_id, content_id, advertisement_id) 
                    VALUES (:userId, :contentId, :advertisementId)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getUserAdStreamingLogToSqlParameterSources(logs));
    }

    private MapSqlParameterSource[] getUserAdStreamingLogToSqlParameterSources(List<UserAdWatchHistory> logs) {
        return logs.stream()
                .map(this::getUserAdStreamingLogToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getUserAdStreamingLogToSqlParameterSource(UserAdWatchHistory log) {
        return new MapSqlParameterSource()
                .addValue("userId", log.getUserId())
                .addValue("contentId", log.getContentId())
                .addValue("advertisementId", log.getAdId());
    }
}
