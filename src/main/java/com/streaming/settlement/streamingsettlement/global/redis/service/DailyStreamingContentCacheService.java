package com.streaming.settlement.streamingsettlement.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.streaming.settlement.streamingsettlement.global.redis.constant.RedisKeyConstants.DAILY_VIEWED_CONTENT_KEY_PREFIX;
import static com.streaming.settlement.streamingsettlement.global.redis.constant.RedisKeyConstants.LOCK_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStreamingContentCacheService {

    private final RedissonClient redissonClient;
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean isExistContentId(Long contentId) {
        String lockKey = generateLockKey(contentId);
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if(!lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                return true;
            }

            try{
                String key = generateDailyStreamingContentKey();
                return redisTemplate.opsForSet().isMember(key, String.valueOf(contentId));
            }finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContentId(Long contentId) {
        String key = generateDailyStreamingContentKey();
        redisTemplate.opsForSet().add(key, String.valueOf(contentId));

        // key가 최초 생성될 때, 만료시간을 설정합니다.
        Long expireTime = redisTemplate.getExpire(key);
        if (expireTime == null || expireTime < 0) {
            setExpired(key);
        }
    }

    private void setExpired(String key) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        Duration timeUntilMidnight = Duration.between(now, midnight);
        redisTemplate.expire(key, timeUntilMidnight.toSeconds(), TimeUnit.SECONDS);
    }

    private String generateDailyStreamingContentKey() {
        return DAILY_VIEWED_CONTENT_KEY_PREFIX + getToday();
    }

    private String generateLockKey(Long contentId) {
        return String.format("%s:%s:%d", LOCK_PREFIX, getToday(), contentId);
    }

    private static LocalDate getToday() {
        return LocalDate.now();
    }

}
