package com.streaming.settlement.streamingsettlement.global.redis.service;

import com.streaming.settlement.streamingsettlement.global.redis.dto.AbusingKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.streaming.settlement.streamingsettlement.global.redis.constant.RedisKeyConstants.ABUSE_KEY_PREFIX;
import static com.streaming.settlement.streamingsettlement.global.redis.constant.RedisKeyConstants.LOCK_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class ViewAbusingCacheService {

    private final RedissonClient redissonClient;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration ABUSE_WINDOW = Duration.ofSeconds(30);

    public boolean isAbusing(AbusingKey key) {
        String lockKey = generateKey(LOCK_PREFIX, key.contentId(), key.userId(), key.ip());
        RLock lock = redissonClient.getLock(lockKey);

        // 락 획득 실패시 어뷰징으로 간주
        try {
            if (!lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                return true;
            }

            try {
                String abuseKey = generateKey(ABUSE_KEY_PREFIX, key.contentId(), key.userId(), key.ip());
                return key.userId().equals(key.creatorId()) || Boolean.TRUE.equals(redisTemplate.hasKey(abuseKey));
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAbusing(AbusingKey key) {
        String abuseKey = generateKey(ABUSE_KEY_PREFIX, key.contentId(), key.userId(), key.ip());
        redisTemplate.opsForValue().set(abuseKey, abuseKey, ABUSE_WINDOW);
    }

    public String generateKey(String keyPrefix, Long contentId, Long userId, String ipAddress) {
        return String.format("%s:contentId:%d:userId:%d:ip:%s", keyPrefix, contentId, userId, ipAddress);
    }

}
