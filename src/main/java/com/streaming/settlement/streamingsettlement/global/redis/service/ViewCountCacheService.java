package com.streaming.settlement.streamingsettlement.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static com.streaming.settlement.streamingsettlement.global.redis.constant.RedisKeyConstants.VIEW_COUNT_KEY_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int BATCH_SIZE = 500;

    /**
     * key : content:viewCount:time:202410271557
     */
    public void incrementViewCount(Long contentId) {
        String key = generateViewCountKey();

        // contentId가 없으면 자동으로 0으로 초기화한 후 1 증가, 있으면 기존 값에 1 증가
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.increment(key, String.valueOf(contentId), 1L);
    }

    /**
     * Redis에서 이전 1분간의 영상별 조회수를 조회합니다.
     * https://redis.io/docs/latest/commands/scan/
     */
    public Map<Long, Long> fetchPreviousMinuteViewCounts(String timeWindowKey) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        ScanOptions options = ScanOptions.scanOptions()
                .match("*")
                .count(BATCH_SIZE)
                .build();
        Cursor<Map.Entry<String, String>> cursor = hashOps.scan(timeWindowKey, options);

        Map<Long, Long> contentViewCount = new HashMap<>();

        while (cursor.hasNext()) {
            Map.Entry<String, String> entry = cursor.next();
            contentViewCount.put(Long.parseLong(entry.getKey()), Long.parseLong(entry.getValue()));
        }

        return contentViewCount;

    }

    public void deleteProcessedKeys(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 직전 1분 동안의 조회수를 조회하기 위한 key를 생성합니다.
     * 예시:
     * - 현재 시간: 2024-10-27 16:10
     * - 생성되는 key: "viewCount:time:202410271609"
     */
    public String generatePreviousMinuteViewCountKey() {
        return VIEW_COUNT_KEY_PREFIX + getPreviousTimeWindow();
    }

    private static String getPreviousTimeWindow() {
        LocalDateTime oneMinuteBefore = LocalDateTime.now().minusMinutes(1);
        return oneMinuteBefore.truncatedTo(ChronoUnit.MINUTES).toString();
    }

    /**
     * 현재 시간에서 초 단위를 절삭 후 Key를 생성합니다
     */
    private static String generateViewCountKey() {
        return VIEW_COUNT_KEY_PREFIX + getTimeWindow(LocalDateTime.now());
    }

    private static String getTimeWindow(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MINUTES).toString();
    }

}
