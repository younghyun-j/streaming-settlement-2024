package com.streaming.settlement.streamingsettlement.statistics.entity;

import com.streaming.settlement.streamingsettlement.streaming.entity.UserAdWatchHistory;
import com.streaming.settlement.streamingsettlement.streaming.entity.UserContentWatchHistory;
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
    private Long playbackTime;

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
                                  Long playbackTime,
                                  Long totalContentViews,
                                  Long totalAdViews,
                                  Long totalPlaybackTime,
                                  LocalDate watchedDate) {
        this.contentId = contentId;
        this.contentViews = contentViews;
        this.adViews = adViews;
        this.playbackTime = playbackTime;
        this.totalContentViews = totalContentViews;
        this.totalAdViews = totalAdViews;
        this.totalPlaybackTime = totalPlaybackTime;
        this.watchedDate = watchedDate;
        this.createdAt = LocalDateTime.now();
    }

    public void updateContentStatistics(List<UserContentWatchHistory> contentWatchHistories) {
        contentViews += contentWatchHistories.size();
        totalContentViews += contentWatchHistories.size();
        contentWatchHistories.forEach(history -> {
            playbackTime += history.getTotalPlaybackTime();
            totalPlaybackTime += history.getTotalPlaybackTime();
        });
    }

    public void updateAdStatistics(List<UserAdWatchHistory> adWatchHistories) {
        adViews += adWatchHistories.size();
        totalAdViews += adWatchHistories.size();
    }
}
