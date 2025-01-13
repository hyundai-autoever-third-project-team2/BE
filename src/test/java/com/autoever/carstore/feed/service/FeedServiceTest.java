package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dto.FeedMapper;
import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.dto.FeedUserResponseDto;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {
    @Mock
    private FeedRepository feedRepository;

    @Mock
    private FeedMapper feedMapper;

    @InjectMocks
    private FeedServiceImpl feedService;

    @Test
    @DisplayName("유효한 데이터를 통해 저장을 시도하면 정상적으로 데이터베이스에 저장되어야 한다.")
    void saveFeed_withValidData_success(){
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        Long feedId = 1L;
        String contents = "test contents";
        boolean isDeleted = false;
        String imageUrl = "image url";

        FeedRequestDto requestDto = FeedRequestDto.builder()
                .userId(user.getUserId())
                .contents(contents)
                .imageUrl(imageUrl)
                .build();

        FeedEntity feed = FeedEntity.builder()
                .feedId(feedId)
                .user(user)
                .contents(contents)
                .isDeleted(isDeleted)
                .imageUrl(imageUrl)
                .build();

        FeedResponseDto responseDto = FeedResponseDto.builder()
                .feedId(feed.getFeedId())
                .contents(feed.getContents())
                .imageUrl(feed.getImageUrl())
                .createdAt(feed.getCreatedAt())
                .user(FeedUserResponseDto.builder()
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .profileImage(feed.getImageUrl())
                        .build())
                .build();

        // Mock 설정
        when(feedMapper.dtoToEntity(requestDto)).thenReturn(feed);
        when(feedRepository.save(any(FeedEntity.class))).thenReturn(feed);
        when(feedMapper.entityToDto(feed)).thenReturn(responseDto);

        // When
        FeedResponseDto result = feedService.saveFeed(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(result.getUser().getUserId(), feed.getUser().getUserId());
        assertEquals(result.getContents(), feed.getContents());
        assertEquals(result.getImageUrl(), feed.getImageUrl());
        assertEquals(result.getFeedId(), feed.getFeedId());
        assertEquals(result.getCreatedAt(), feed.getCreatedAt());
        verify(feedRepository, times(1)).save(any(FeedEntity.class));
        verify(feedMapper, times(1)).entityToDto(feed);
    }

    @Test
    @DisplayName("contents 값이 null이어도 데이터베이스에 저장되어야 한다.")
    void saveFeed_withNullContents_success(){
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        Long feedId = 1L;
        boolean isDeleted = false;
        String imageUrl = "image url";

        FeedRequestDto requestDto = FeedRequestDto.builder()
                .userId(user.getUserId())
                .imageUrl(imageUrl)
                .build();

        FeedEntity feed = FeedEntity.builder()
                .feedId(feedId)
                .user(user)
                .isDeleted(isDeleted)
                .imageUrl(imageUrl)
                .build();

        FeedResponseDto responseDto = FeedResponseDto.builder()
                .feedId(feed.getFeedId())
                .imageUrl(feed.getImageUrl())
                .createdAt(feed.getCreatedAt())
                .user(FeedUserResponseDto.builder()
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .profileImage(feed.getImageUrl())
                        .build())
                .build();

        // Mock 설정
        when(feedMapper.dtoToEntity(requestDto)).thenReturn(feed);
        when(feedRepository.save(any(FeedEntity.class))).thenReturn(feed);
        when(feedMapper.entityToDto(feed)).thenReturn(responseDto);

        // When
        FeedResponseDto result = feedService.saveFeed(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(result.getUser().getUserId(), feed.getUser().getUserId());
        assertEquals(result.getImageUrl(), feed.getImageUrl());
        assertEquals(result.getFeedId(), feed.getFeedId());
        assertEquals(result.getCreatedAt(), feed.getCreatedAt());
        verify(feedRepository, times(1)).save(any(FeedEntity.class));
        verify(feedMapper, times(1)).entityToDto(feed);
    }

    @Test
    @DisplayName("user 값이 null이라면 IllegalArgumentException이 발생한다.")
    void saveFeed_withNullUser_throwsIllegalArgumentException(){
        // Given
        String contents = "test contents";
        String imageUrl = "image url";

        FeedRequestDto requestDto = FeedRequestDto.builder()
                .userId(null)
                .contents(contents)
                .imageUrl(imageUrl)
                .build();

        // When & Then
        assertThatThrownBy(() -> feedService.saveFeed(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("user can't be null");
        verify(feedRepository, never()).save(any());
    }

    @Test
    @DisplayName("imageUrl 값이 null이라면 IllegalArgumentException이 발생한다.")
    void saveFeed_withNullImageUrl_throwsIllegalArgumentException(){
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        String contents = "test contents";
        String imageUrl = null;

        FeedRequestDto requestDto = FeedRequestDto.builder()
                .userId(user.getUserId())
                .contents(contents)
                .imageUrl(imageUrl)
                .build();

        // When & Then
        assertThatThrownBy(() -> feedService.saveFeed(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("imageUrl can't be null");
        verify(feedRepository, never()).save(any());
    }

    @Test
    @DisplayName("유효한 데이터를 통해 삭제를 시도하면 정상적으로 데이터베이스의 isDeleted 상태값이 true가 되어야 한다.")
    void deleteFeed_withValidData_success(){
        // Given
        Long userId = 1L;

        UserEntity user = UserEntity.builder()
                .userId(userId)
                .nickname("test")
                .build();

        Long feedId = 1L;
        String contents = "test contents";
        boolean isDeleted = false;
        String imageUrl = "image url";

        FeedEntity feed = FeedEntity.builder()
                .feedId(feedId)
                .user(user)
                .contents(contents)
                .isDeleted(isDeleted)
                .imageUrl(imageUrl)
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        when(feedRepository.save(any(FeedEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Boolean result = feedService.deleteFeed(userId, feedId);

        // Then
        assertTrue(result);
        assertTrue(feed.isDeleted());
        verify(feedRepository, times(1)).findById(feedId);
        verify(feedRepository, times(1)).save(any(FeedEntity.class));
    }

    @Test
    @DisplayName("userId 값이 null이라면 IllegalArgumentException이 발생한다.")
    void deleteFeed_withNullUserId_throwsIllegalArgumentException(){
        // Given
        Long userId = null;
        Long feedId = 1L;

        // When & Then
        assertThatThrownBy(() -> feedService.deleteFeed(userId, feedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("userId can't be null");
        verify(feedRepository, never()).save(any());
    }
    @Test
    @DisplayName("userId 값이 null이라면 IllegalArgumentException이 발생한다.")
    void deleteFeed_withNullFeedId_throwsIllegalArgumentException(){
        // Given
        Long userId = 1L;
        Long feedId = null;

        // When & Then
        assertThatThrownBy(() -> feedService.deleteFeed(userId, feedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("feedId can't be null");
        verify(feedRepository, never()).save(any());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 피드를 삭제하려고 하면 되어야 한다.")
    void deleteFeed_withFeedByOtherUser_throwsIllegalArgumentException(){
        // Given
        Long ownerId = 1L;
        Long otherUserId = 2L;

        UserEntity owner = UserEntity.builder()
                .userId(ownerId)
                .build();

        Long feedId = 1L;

        FeedEntity feed = FeedEntity.builder()
                .feedId(feedId)
                .user(owner)
                .contents("test contents")
                .isDeleted(false)
                .imageUrl("image url")
                .build();

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When & Then
        assertThatThrownBy(() -> feedService.deleteFeed(otherUserId, feedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("It's not this user's feed");

        verify(feedRepository, times(1)).findById(feedId);
        verify(feedRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 feedId에 대해 삭제를 하려고 하면 IllegalArgumentException이 발생한다.")
    void deleteFeed_withNonExistFeedId_throwsIllegalArgumentException(){
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .build();

        Long feedId = 1L;

        when(feedRepository.findById(feedId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> feedService.deleteFeed(user.getUserId(), feedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There's no such feedId");

        verify(feedRepository, times(1)).findById(feedId);
        verify(feedRepository, never()).save(any());
    }

    @Test
    @DisplayName("피드 리스트를 조회하면 피드들의 리스트가 최신 순으로 조회되어야 한다.")
    void findFeedList_whenFeedsExist_success() {
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("testUser")
                .build();

        FeedEntity feed1 = FeedEntity.builder()
                .feedId(1L)
                .user(user)
                .contents("Feed 1")
                .isDeleted(false)
                .build();

        ReflectionTestUtils.setField(feed1, "createdAt", LocalDateTime.now().minusDays(1));

        FeedEntity feed2 = FeedEntity.builder()
                .feedId(2L)
                .user(user)
                .contents("Feed 2")
                .isDeleted(false)
                .build();

        ReflectionTestUtils.setField(feed2, "createdAt", LocalDateTime.now());

        FeedEntity feed3 = FeedEntity.builder()
                .feedId(3L)
                .user(user)
                .contents("Feed 3")
                .isDeleted(true)
                .build();

        ReflectionTestUtils.setField(feed3, "createdAt", LocalDateTime.now().minusDays(2));

        List<FeedEntity> mockFeedList = List.of(feed2, feed1);

        // Mock 설정
        when(feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc()).thenReturn(mockFeedList);

        when(feedMapper.entityToDto(feed1)).thenReturn(
                FeedResponseDto.builder()
                        .feedId(feed1.getFeedId())
                        .contents(feed1.getContents())
                        .imageUrl(feed1.getImageUrl())
                        .createdAt(feed1.getCreatedAt())
                        .build()
        );

        when(feedMapper.entityToDto(feed2)).thenReturn(
                FeedResponseDto.builder()
                        .feedId(feed2.getFeedId())
                        .contents(feed2.getContents())
                        .imageUrl(feed2.getImageUrl())
                        .createdAt(feed2.getCreatedAt())
                        .build()
        );

        // When
        List<FeedResponseDto> result = feedService.findFeedList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(feed2.getFeedId(), result.get(0).getFeedId());
        assertEquals(feed1.getFeedId(), result.get(1).getFeedId());

        verify(feedRepository, times(1)).findByIsDeletedFalseOrderByCreatedAtDesc();
        verify(feedMapper).entityToDto(feed1);
        verify(feedMapper).entityToDto(feed2);
    }

    @Test
    @DisplayName("피드 리스트를 조회하면 피드가 없더라도 빈 리스트가 조회되어야 한다.")
    void findFeedList_whenFeedsNotExist_returnEmptyList() {
        // Given
        List<FeedEntity> emptyList = new ArrayList<>(); // ArrayList로 빈 리스트 생성

        // Mock: findAllByIsDeletedFalseOrderByCreatedAtDesc 호출 시 반환값 설정
        when(feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc()).thenReturn(emptyList);

        // When
        List<FeedResponseDto> result = feedService.findFeedList();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(feedRepository, times(1)).findByIsDeletedFalseOrderByCreatedAtDesc();
    }
}
