package com.streaming.settlement.streamingsettlement.streaming.controller;

import com.streaming.settlement.streamingsettlement.content.service.ContentService;
import com.streaming.settlement.streamingsettlement.global.redis.dto.AbusingKey;
import com.streaming.settlement.streamingsettlement.global.redis.service.ViewAbusingCacheService;
import com.streaming.settlement.streamingsettlement.global.redis.service.ViewCountCacheService;
import com.streaming.settlement.streamingsettlement.global.util.IpUtil;
import com.streaming.settlement.streamingsettlement.streaming.dto.request.UserStreamingReportReq;
import com.streaming.settlement.streamingsettlement.streaming.dto.response.StreamingContentRes;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.service.DailyWatchedContentHistoryService;
import com.streaming.settlement.streamingsettlement.streaming.service.StreamingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final StreamingService streamingService;
    private final DailyWatchedContentHistoryService dailyWatchedContentHistoryService;
    private final ContentService contentService;
    private final ViewAbusingCacheService viewAbusingCacheService;
    private final ViewCountCacheService viewCountCacheService;

    // TODO : 인증 구현시 userId param 제거
    @GetMapping("/contents/{contentId}")
    public ResponseEntity<StreamingContentRes> startStreaming(HttpServletRequest request,
                                                              @RequestParam Long userId,
                                                              @PathVariable Long contentId) {
        StreamingContentRes response = streamingService.getStreamingContent(userId, contentId);
        AbusingKey abusingKey = createAbusingKey(request, userId, response);
        boolean isAbusing = viewAbusingCacheService.isAbusing(abusingKey);

        if (!isAbusing) {
            dailyWatchedContentHistoryService.addDailyStreamingContentId(contentId, response.playbackStartDate());
            viewCountCacheService.incrementViewCount(contentId); // 조회수 증가
            viewAbusingCacheService.setAbusing(abusingKey); // 어뷰징 등록
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/contents/{contentId}")
    public ResponseEntity<Void> endStreaming(@RequestParam Long userId,
                                             @PathVariable Long contentId,
                                             @RequestBody UserStreamingReportReq request) {

        contentService.isExistContent(contentId);

        UserContentWatchHistory userContentWatchHistory = request.toUserContentStreamingLog(userId, contentId);
        List<UserAdWatchHistory> userAdvertisementViewedHistories = request.toUserAdStreamingLogs(userId, contentId);
        streamingService.endStreamingContent(userContentWatchHistory, userAdvertisementViewedHistories);

        return ResponseEntity.noContent().build();
    }

    private static AbusingKey createAbusingKey(HttpServletRequest request, Long userId, StreamingContentRes response) {
        return new AbusingKey(
                userId,
                response.contentId(),
                response.creatorId(),
                IpUtil.getClientIp(request));
    }

}
