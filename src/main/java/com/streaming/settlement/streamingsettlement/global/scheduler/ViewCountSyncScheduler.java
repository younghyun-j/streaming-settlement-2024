package com.streaming.settlement.streamingsettlement.global.scheduler;

import com.streaming.settlement.streamingsettlement.content.repository.ContentRepository;
import com.streaming.settlement.streamingsettlement.global.redis.service.ViewCountCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final ContentRepository contentRepository;
    private final ViewCountCacheService viewCountCacheService;

    /**
     * 매분 5초에 실행되어 이전 1분간의 영상 조회수를 DB에 반영합니다.
     * 예시: 16:01:05에 실행되면 16:00:00~16:00:59 동안의 조회수 집계
     */
    @Scheduled(cron = "5 * * * * *")
    @Transactional
    public void syncContentViewCountsToDatabase() {
        String timeWindowKey = viewCountCacheService.generatePreviousMinuteViewCountKey();
        Map<Long, Long> viewCounts = viewCountCacheService.fetchPreviousMinuteViewCounts(timeWindowKey);

        while(!viewCounts.isEmpty()) {
            try{
                contentRepository.bulkUpdateViewCounts(viewCounts);
                viewCountCacheService.deleteProcessedKeys(timeWindowKey);
                viewCounts = viewCountCacheService.fetchPreviousMinuteViewCounts(timeWindowKey);

            }catch (Exception e){
                // TODO : 에러처리
                throw new RuntimeException(e);
            }
        }
    }
}
