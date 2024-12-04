package com.streaming.settlement.streamingsettlement.streaming.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyWatchedContentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private LocalDate watchedDate; // 시청 일자

    private LocalDateTime createdAt; // 로그 생성 일자

    public DailyWatchedContentHistory(Long contentId, LocalDate watchedDate) {
        this.contentId = contentId;
        this.watchedDate = watchedDate;
        this.createdAt = LocalDateTime.now();
    }
}
