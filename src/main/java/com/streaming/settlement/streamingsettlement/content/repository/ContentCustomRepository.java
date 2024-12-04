package com.streaming.settlement.streamingsettlement.content.repository;

import java.util.Map;

public interface ContentCustomRepository {
    void bulkUpdateViewCounts(Map<Long, Long> updateViewCounts);
}
