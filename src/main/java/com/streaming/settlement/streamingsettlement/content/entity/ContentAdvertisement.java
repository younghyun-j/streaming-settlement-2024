package com.streaming.settlement.streamingsettlement.content.entity;

import com.streaming.settlement.streamingsettlement.advertisement.entity.Advertisement;
import com.streaming.settlement.streamingsettlement.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentAdvertisement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cost;

    @ColumnDefault("0")
    private Long views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisemnet_id")
    private Advertisement advertisement;

    public ContentAdvertisement(int cost, Content content, Advertisement advertisement) {
        this.cost = cost;
        this.content = content;
        this.advertisement = advertisement;
    }
}
