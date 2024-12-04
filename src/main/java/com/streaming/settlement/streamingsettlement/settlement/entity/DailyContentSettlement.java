package com.streaming.settlement.streamingsettlement.settlement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyContentSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long contentRevenue;

    @Column(nullable = false)
    private Long adRevenue;

    @Column(nullable = false)
    private Long totalContentRevenue;

    @Column(nullable = false)
    private Long totalAdRevenue;

    @Column(nullable = false)
    private LocalDate watchedDate;

    private LocalDateTime createdAt;

    @Builder
    public DailyContentSettlement(Long contentId,
                                  Long contentRevenue,
                                  Long adRevenue,
                                  Long totalContentRevenue,
                                  Long totalAdRevenue,
                                  LocalDate watchedDate) {
        this.contentId = contentId;
        this.contentRevenue = contentRevenue;
        this.adRevenue = adRevenue;
        this.totalContentRevenue = totalContentRevenue;
        this.totalAdRevenue = totalAdRevenue;
        this.watchedDate = watchedDate;
    }
}
