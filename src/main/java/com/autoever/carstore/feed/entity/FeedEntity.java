package com.autoever.carstore.feed.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="feed")
public class FeedEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id", unique = true, nullable = false)
    private long feedId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "contents")
    private String contents;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public void udpateContents(String contents) {
        this.contents = contents;
    }

    public void udpateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateIsDeleted() {
        this.isDeleted = true;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
