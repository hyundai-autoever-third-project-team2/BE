package com.autoever.carstore.feed.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed_like")
public class FeedLikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_like_id", unique = true, nullable = false)
    private Long feedLikeId;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private FeedEntity feed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
