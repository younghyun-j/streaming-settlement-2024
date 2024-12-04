package com.streaming.settlement.streamingsettlement.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    private static final String REDISSON_URL_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisAddress = REDISSON_URL_PREFIX + redisHost + ":" + redisPort;
        config.useSingleServer()
                .setAddress(redisAddress)
                .setSubscriptionConnectionMinimumIdleSize(1) // 각 서버는 분산락 상태 감지를 위한 구독 연결 1개 유지
                .setSubscriptionConnectionPoolSize(2);  // 부하 상황에서 최대 2개까지 구독 연결 가능
        return Redisson.create(config);
    }

}
