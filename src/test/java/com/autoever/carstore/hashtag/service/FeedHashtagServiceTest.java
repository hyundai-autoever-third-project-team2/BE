package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.repository.FeedHashtagRepository;
import com.autoever.carstore.hashtag.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedHashtagServiceTest {

    @Mock
    private FeedHashtagRepository feedHashtagRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    @InjectMocks
    private FeedHashtagServiceImpl feedHashtagService;

    @Test
    @DisplayName("피드와 해시태그 ID로 피드-해시태그 관계를 저장하면 FeedHashTagResponseDto가 반환된다")
    void saveFeedHashtag_success() {
        // Given
        Long feedId = 1L;
        Long hashtagId = 1L;

        FeedEntity feed = FeedEntity.builder()
                .feedId(feedId)
                .build();

        HashtagEntity hashtag = HashtagEntity.builder()
                .hashtagId(hashtagId)
                .hashtag("test")
                .build();

        FeedHashtagEntity feedHashtag = FeedHashtagEntity.builder()
                .feed(feed)
                .hashtag(hashtag)
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));
        when(hashtagRepository.findById(hashtagId)).thenReturn(Optional.of(hashtag));
        when(feedHashtagRepository.save(any(FeedHashtagEntity.class))).thenReturn(feedHashtag);

        // When
        FeedHashTagResponseDto result = feedHashtagService.saveFeedHashtag(feedId, hashtagId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFeedId()).isEqualTo(feedId);
        assertThat(result.getHashTag()).isEqualTo(hashtag.getHashtag());
        verify(feedRepository).findById(feedId);
        verify(hashtagRepository).findById(hashtagId);
        verify(feedHashtagRepository).save(any(FeedHashtagEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 저장을 시도하면 IllegalArgumentException이 발생한다")
    void saveFeedHashtag_withInvalidFeedId_throwsException() {
        // Given
        Long invalidFeedId = 999L;
        Long hashtagId = 1L;

        when(feedRepository.findById(invalidFeedId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedHashtagService.saveFeedHashtag(invalidFeedId, hashtagId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There's no such feedId");
        verify(feedRepository).findById(invalidFeedId);
    }

    @Test
    @DisplayName("존재하지 않는 해시태그 ID로 저장을 시도하면 IllegalArgumentException이 발생한다")
    void saveFeedHashtag_withInvalidHashtagId_throwsException() {
        // Given
        Long feedId = 1L;
        Long invalidHashtagId = 999L;

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(FeedEntity.builder().feedId(feedId).build()));
        when(hashtagRepository.findById(invalidHashtagId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedHashtagService.saveFeedHashtag(feedId, invalidHashtagId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There's no such hashtagId");
        verify(feedRepository).findById(feedId);
        verify(hashtagRepository).findById(invalidHashtagId);
    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 저장을 시도하면 IllegalArgumentException이 발생한다")
    void saveFeedHashtag_withNullFeedId_throwsException() {
        // Given
        Long invalidFeedId = null;
        Long hashtagId = 1L;

        // When & Then
        assertThatThrownBy(() -> feedHashtagService.saveFeedHashtag(invalidFeedId, hashtagId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("feedId cannot be null");
    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 저장을 시도하면 IllegalArgumentException이 발생한다")
    void saveFeedHashtag_withNullHashtagId_throwsException() {
        // Given
        Long invalidFeedId = 1L;
        Long hashtagId = null;

        // When & Then
        assertThatThrownBy(() -> feedHashtagService.saveFeedHashtag(invalidFeedId, hashtagId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("hashtagId cannot be null");
    }

    @Test
    @DisplayName("피드 ID로 해당 피드의 해시태그 목록을 조회한다")
    void getFeedHashtagList_success() {
        // Given
        Long feedId = 1L;
        FeedEntity feed = FeedEntity.builder().feedId(feedId).build();

        List<FeedHashtagEntity> feedHashtags = List.of(
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(HashtagEntity.builder()
                                .hashtagId(1L)
                                .hashtag("tag1")
                                .build())
                        .build(),
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(HashtagEntity.builder()
                                .hashtagId(2L)
                                .hashtag("tag2")
                                .build())
                        .build()
        );

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));
        when(feedHashtagRepository.findByFeed(feed)).thenReturn(feedHashtags);

        // When
        List<HashtagResponseDto> results = feedHashtagService.getFeedHashtagList(feedId);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("hashtag")
                .containsExactlyInAnyOrder("tag1", "tag2");
    }

    @Test
    @DisplayName("피드 ID로 해당 피드의 해시태그 목록을 조회할 때 해시태그가 없다면 빈 리스트가 반환된다")
    void getFeedHashtagList_whenNoHashtags_returnEmptyList() {
        // Given
        Long feedId = 1L;
        FeedEntity feed = FeedEntity.builder().feedId(feedId).build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));
        when(feedHashtagRepository.findByFeed(feed)).thenReturn(List.of());

        // When
        List<HashtagResponseDto> results = feedHashtagService.getFeedHashtagList(feedId);

        // Then
        assertThat(results).isEmpty();
        verify(feedRepository).findById(feedId);
        verify(feedHashtagRepository).findByFeed(feed);
    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 해시태그 목록을 조회하면 IllegalArgumentException이 발생한다")
    void getFeedHashtagList_withInvalidFeedId_throwsException() {
        // Given
        Long invalidFeedId = 999L;

        when(feedRepository.findById(invalidFeedId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedHashtagService.getFeedHashtagList(invalidFeedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There's no such feedId");
    }
}
