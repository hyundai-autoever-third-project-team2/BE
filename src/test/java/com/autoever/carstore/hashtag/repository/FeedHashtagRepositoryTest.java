package com.autoever.carstore.hashtag.repository;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureTestEntityManager
public class FeedHashtagRepositoryTest {

    @Autowired
    private FeedHashtagRepository feedHashtagRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("FeedHashtagEntity를 저장하면 데이터베이스에 저장되어야 한다.")
    void saveFeedHashtag_success() {
        // Given
        UserEntity user = userRepository.save(UserEntity.builder()
                .userId(1L)
                .email("test@test.com")
                .nickname("test")
                .profileImage("default-profile.jpg")
                .isActive(true)
                .survey(false)
                .userRole("ROLE_USER")  // 기본 권한
                .build());

        FeedEntity feed = feedRepository.save(FeedEntity.builder()
                .user(user)
                .contents("test")
                .isDeleted(false)
                .imageUrl("test-url")
                .build());

        HashtagEntity hashtag = hashtagRepository.save(HashtagEntity.builder()
                .hashtag("testHashtag")
                .build());

        FeedHashtagEntity feedHashtag = FeedHashtagEntity.builder()
                .feed(feed)
                .hashtag(hashtag)
                .build();

        // When
        FeedHashtagEntity savedEntity = feedHashtagRepository.save(feedHashtag);

        // Then
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getFeed()).isEqualTo(feed);
        assertThat(savedEntity.getHashtag()).isEqualTo(hashtag);
    }

    @Test
    @DisplayName("Feed 엔터티를 사용하여 FeedHashtagEntity 목록을 조회할 수 있어야 한다.")
    void findByFeed_success() {
        // Given
        UserEntity user = userRepository.save(UserEntity.builder()
                .userId(1L)
                .email("test@test.com")
                .nickname("test")
                .profileImage("default-profile.jpg")
                .isActive(true)
                .survey(false)
                .userRole("ROLE_USER")  // 기본 권한
                .build());

        FeedEntity feed = feedRepository.save(FeedEntity.builder()
                .user(user)
                .contents("test")
                .isDeleted(false)
                .imageUrl("test-url")
                .build());

        HashtagEntity hashtag1 = hashtagRepository.save(HashtagEntity.builder()
                .hashtag("testHashtag1")
                .build());

        HashtagEntity hashtag2 = hashtagRepository.save(HashtagEntity.builder()
                .hashtag("testHashtag2")
                .build());

        feedHashtagRepository.save(FeedHashtagEntity.builder()
                .feed(feed)
                .hashtag(hashtag1)
                .build());

        feedHashtagRepository.save(FeedHashtagEntity.builder()
                .feed(feed)
                .hashtag(hashtag2)
                .build());

        // When
        List<FeedHashtagEntity> result = feedHashtagRepository.findByFeed(feed);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("hashtag").containsExactlyInAnyOrder(hashtag1, hashtag2);
    }

    @Test
    @DisplayName("Feed와 Hashtag로 존재 여부를 확인할 수 있어야 한다.")
    void existsByFeedAndHashtag_success() {
        // Given
        UserEntity user = userRepository.save(UserEntity.builder()
                .userId(1L)
                .email("test@test.com")
                .nickname("test")
                .profileImage("default-profile.jpg")
                .isActive(true)
                .survey(false)
                .userRole("ROLE_USER")  // 기본 권한
                .build());

        FeedEntity feed = feedRepository.save(FeedEntity.builder()
                .user(user)
                .contents("test")
                .isDeleted(false)
                .imageUrl("test-url")
                .build());

        HashtagEntity hashtag = hashtagRepository.save(HashtagEntity.builder()
                .hashtag("testHashtag")
                .build());

        feedHashtagRepository.save(FeedHashtagEntity.builder()
                .feed(feed)
                .hashtag(hashtag)
                .build());

        // When
        boolean exists = feedHashtagRepository.existsByFeedAndHashtag(feed, hashtag);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 Feed와 Hashtag로 존재 여부를 확인하면 false를 반환해야 한다.")
    void existsByFeedAndHashtag_notExist() {
        // Given
        FeedEntity feed = FeedEntity.builder()
                .feedId(1L)
                .build();

        HashtagEntity hashtag = HashtagEntity.builder()
                .hashtagId(1L)
                .hashtag("testHashtag")
                .build();

        // When
        boolean exists = feedHashtagRepository.existsByFeedAndHashtag(feed, hashtag);

        // Then
        assertThat(exists).isFalse();
    }
}
