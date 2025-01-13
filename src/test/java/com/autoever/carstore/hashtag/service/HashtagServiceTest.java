package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @Mock
    private HashtagRepository hashtagRepository;

    @Mock
    private FeedHashtagRepository feedHashtagRepository;

    @Mock
    private FeedRepository feedRepository;

    @InjectMocks
    private HashtagServiceImpl hashtagService;

    @Test
    @DisplayName("해시태그를 저장하면 HashtagResponseDto가 반환된다")
    void saveHashtag_success() {
        // Given
        String hashtag = "test";
        HashtagEntity entity = HashtagEntity.builder()
                .hashtag(hashtag)
                .build();

        when(hashtagRepository.save(any(HashtagEntity.class))).thenReturn(entity);

        // When
        HashtagResponseDto result = hashtagService.saveHashtag(hashtag);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getHashtag()).isEqualTo(hashtag);
        verify(hashtagRepository).save(any());
    }

    @Test
    @DisplayName("해시태그를 저장하면 HashtagResponseDto가 반환된다")
    void saveHashtag_withNullHashtag_throwsIllegalArgumentException() {
        // Given
        String hashtag = null;
        HashtagEntity entity = HashtagEntity.builder()
                .hashtag(hashtag)
                .build();

        // When & Then
        assertThatThrownBy(() -> hashtagService.saveHashtag(hashtag))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("hashtag can't be null");
    }

    @Test
    @DisplayName("피드 ID로 해시태그 목록을 조회하면 HashtagResponseDto 리스트가 반환된다")
    void getHashtagList_success() {
        // Given
        Long feedId = 1L;
        FeedEntity feed = FeedEntity.builder().feedId(feedId).build();

        List<FeedHashtagEntity> feedHashtags = List.of(
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(HashtagEntity.builder().hashtag("tag1").build())
                        .build(),
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(HashtagEntity.builder().hashtag("tag2").build())
                        .build()
        );

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));
        when(feedHashtagRepository.findByFeed(feed)).thenReturn(feedHashtags);

        // When
        List<HashtagResponseDto> results = hashtagService.getHashtagList(feedId);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("hashtag")
                .containsExactlyInAnyOrder("tag1", "tag2");
        verify(feedRepository).findById(feedId);
        verify(feedHashtagRepository).findByFeed(feed);
    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 조회하면 IllegalArgumentException이 발생한다")
    void getHashtagList_withInvalidFeedId_throwsIllegalArgumentException() {
        // Given
        Long invalidFeedId = 999L;
        when(feedRepository.findById(invalidFeedId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> hashtagService.getHashtagList(invalidFeedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There's no hashtag on this feedId");
        verify(feedRepository).findById(invalidFeedId);
    }
}
