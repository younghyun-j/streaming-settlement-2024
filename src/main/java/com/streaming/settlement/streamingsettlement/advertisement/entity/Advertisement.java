package com.streaming.settlement.streamingsettlement.advertisement.entity;

import com.streaming.settlement.streamingsettlement.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertisement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @ColumnDefault("true")
    private boolean isActive;

    @ColumnDefault("0")
    private Long views;

    public Advertisement(String title, String url, boolean isActive) {
        this.title = title;
        this.url = url;
        this.isActive = isActive;
    }
}
