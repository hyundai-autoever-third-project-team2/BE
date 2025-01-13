package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.service.FeedService;
import com.autoever.carstore.s3.ImageUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {

    @Mock
    private FeedService feedService;

    @Mock
    private ImageUploadService imageUploadService;

    @InjectMocks
    private FeedController feedController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(feedController)
                .build();
    }

    @Test
    @DisplayName("피드 작성 API 호출 시 이미지가 S3에 업로드되고 피드가 저장되어야 한다")
    void writeFeed_success() throws Exception {
        // Given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        String imageUrl = "https://s3-bucket.com/test.jpg";
        Long userId = 1L;
        String contents = "test content";

        FeedResponseDto responseDto = FeedResponseDto.builder()
                .feedId(1L)
                .contents(contents)
                .imageUrl(imageUrl)
                .build();

        when(imageUploadService.upload(any(MultipartFile.class))).thenReturn(imageUrl);
        when(feedService.saveFeed(any(FeedRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(multipart("/feed/write")
                        .file(image)
                        .param("userId", userId.toString())
                        .param("contents", contents))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.feedId").value(1L))
                .andExpect(jsonPath("$.imageUrl").value(imageUrl));

        verify(imageUploadService).upload(any(MultipartFile.class));
        verify(feedService).saveFeed(any(FeedRequestDto.class));
    }

    @Test
    @DisplayName("피드 삭제 API 호출 시 피드가 삭제되어야 한다")
    void deleteFeed_success() throws Exception {
        // Given
        Long userId = 1L;
        Long feedId = 1L;

        when(feedService.deleteFeed(userId, feedId)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/feed/delete")
                        .param("userId", userId.toString())
                        .param("feedId", feedId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(feedService).deleteFeed(userId, feedId);
    }

    @Test
    @DisplayName("피드 목록 조회 API 호출 시 피드 목록이 반환되어야 한다")
    void getFeedList_success() throws Exception {
        // Given
        List<FeedResponseDto> feedList = List.of(
                FeedResponseDto.builder()
                        .feedId(1L)
                        .contents("test1")
                        .imageUrl("url1")
                        .build(),
                FeedResponseDto.builder()
                        .feedId(2L)
                        .contents("test2")
                        .imageUrl("url2")
                        .build()
        );

        when(feedService.findFeedList()).thenReturn(feedList);

        // When & Then
        mockMvc.perform(get("/feed/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedId").value(1L))
                .andExpect(jsonPath("$[1].feedId").value(2L));

        verify(feedService).findFeedList();
    }

    @Test
    @DisplayName("피드 작성 시 이미지가 없으면 400 에러가 발생해야 한다")
    void writeFeed_withoutImage_fail() throws Exception {
        mockMvc.perform(multipart("/feed/write")
                        .param("userId", "1")
                        .param("contents", "test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("피드 작성 시 userId가 없으면 400 에러가 발생해야 한다")
    void writeFeed_withoutUserId_fail() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        mockMvc.perform(multipart("/feed/write")
                        .file(image)
                        .param("contents", "test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("피드 삭제 시 존재하지 않는 feedId면 404 에러가 발생해야 한다")
    void deleteFeed_withNonExistentFeed_fail() throws Exception {
        when(feedService.deleteFeed(any(), any()))
                .thenThrow(new IllegalArgumentException("There's no such feedId"));

        mockMvc.perform(put("/feed/delete")
                        .param("userId", "1")
                        .param("feedId", "999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("피드 삭제 시 다른 사용자의 피드를 삭제하려고 하면 403 에러가 발생해야 한다")
    void deleteFeed_withUnauthorizedUser_fail() throws Exception {
        when(feedService.deleteFeed(any(), any()))
                .thenThrow(new IllegalArgumentException("It's not this user's feed"));

        mockMvc.perform(put("/feed/delete")
                        .param("userId", "2")
                        .param("feedId", "1"))
                .andExpect(status().isForbidden());
    }
}