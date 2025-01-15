package com.autoever.carstore.hashtag.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="hashtag")
public class HashtagEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private long hashtagId;

    @Column(nullable = false)
    private String hashtag;

    public void updateHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
