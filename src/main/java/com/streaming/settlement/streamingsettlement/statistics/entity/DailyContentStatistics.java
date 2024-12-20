package com.streaming.settlement.streamingsettlement.statistics.entity;

import com.streaming.settlement.streamingsettlement.global.batch.dto.UserAdWatchHistoryDto;
import com.streaming.settlement.streamingsettlement.global.batch.dto.UserContentWatchHistoryDto;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyContentStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long contentViews;

    @Column(nullable = false)
    private Long adViews;

    @Column(nullable = false)
    private Long totalContentViews;

    @Column(nullable = false)
    private Long totalAdViews;

    @Column(nullable = false)
    private Long totalPlaybackTime;

    @Column(nullable = false)
    private LocalDate watchedDate;

    private LocalDateTime createdAt;

    @Builder
    public DailyContentStatistics(Long contentId,
                                  Long contentViews,
                                  Long adViews,
                                  Long totalContentViews,
                                  Long totalAdViews,
                                  Long totalPlaybackTime,
                                  LocalDate watchedDate) {
        this.contentId = contentId;
        this.contentViews = contentViews;
        this.adViews = adViews;
        this.totalContentViews = totalContentViews;
        this.totalAdViews = totalAdViews;
        this.totalPlaybackTime = totalPlaybackTime;
        this.watchedDate = watchedDate;
        this.createdAt = LocalDateTime.now();
    }

    public void updateContentStatistics(List<UserContentWatchHistoryDto> contentWatchHistories) {
        contentViews += contentWatchHistories.size();
        totalContentViews += contentWatchHistories.size();
        contentWatchHistories.forEach(history -> {
            totalPlaybackTime += history.totalPlaybackTime();
        });
    }

    public void updateAdStatistics(List<UserAdWatchHistoryDto> adWatchHistories) {
        adViews += adWatchHistories.size();
        totalAdViews += adWatchHistories.size();
    }
}
