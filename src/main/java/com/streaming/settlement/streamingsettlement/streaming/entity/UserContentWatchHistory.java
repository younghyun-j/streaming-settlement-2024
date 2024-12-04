package com.streaming.settlement.streamingsettlement.streaming.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(indexes = @Index(name = "idx_watched_date_content_id", columnList = "watched_date, content_id"))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserContentWatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long lastPlaybackPosition; // 마지막 재생 위치

    @Column(nullable = false)
    private Long totalPlaybackTime; // 총 재생 시간

    private LocalDate watchedDate; // 시청 일자

    private LocalDateTime createdAt; // 로그 생성 일자

    public UserContentWatchHistory(Long userId,
                                   Long contentId,
                                   Long lastPlaybackPosition,
                                   Long totalPlaybackTime,
                                   LocalDate watchedDate) {
        this.userId = userId;
        this.contentId = contentId;
        this.lastPlaybackPosition = lastPlaybackPosition;
        this.totalPlaybackTime = totalPlaybackTime;
        this.watchedDate = watchedDate;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isTotalPlaybackTimeExceedsLastPlay(){
        return totalPlaybackTime > lastPlaybackPosition;
    }

    public boolean isPlaybackStartDateBeyondCurrent(){
        return watchedDate.isBefore(LocalDate.now());
    }

}

