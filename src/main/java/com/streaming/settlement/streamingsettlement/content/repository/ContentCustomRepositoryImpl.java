package com.streaming.settlement.streamingsettlement.content.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ContentCustomRepositoryImpl implements ContentCustomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    @Override
    public void bulkUpdateViewCounts(Map<Long, Long> updateViewCounts) {
        String sql = """
                UPDATE content 
                SET views = COALESCE(views, 0) + :count, 
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = :contentId
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getUpdateViewCountToSqlParameterSources(updateViewCounts));
    }

    private MapSqlParameterSource[] getUpdateViewCountToSqlParameterSources(Map<Long, Long> updateViewCounts) {
        return updateViewCounts.entrySet().stream()
                .map(viewCount -> getUpdateViewCountToSqlParameterSource(viewCount.getKey(), viewCount.getValue()))
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getUpdateViewCountToSqlParameterSource(Long contentId, Long viewCount) {
        return new MapSqlParameterSource()
                .addValue("contentId", contentId)
                .addValue("count", viewCount);
    }
}
