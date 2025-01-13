//package com.autoever.carstore.feed.controller;
//
//import com.autoever.carstore.feed.FeedController;
//import com.autoever.carstore.feed.dto.FeedRequestDto;
//import com.autoever.carstore.feed.dto.FeedResponseDto;
//import com.autoever.carstore.feed.dto.FeedUserResponseDto;
//import com.autoever.carstore.feed.entity.FeedEntity;
//import com.autoever.carstore.feed.service.FeedService;
//import com.autoever.carstore.user.entity.UserEntity;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(FeedController.class)
//public class FeedControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private FeedService feedService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("POST /feeds/write - 피드 생성 테스트")
//    void createFeed_success() throws Exception {
//        // Given
//        Long userId = 1L;
//        Long feedId = 1L;
//        String contents = "New feed";
//        String imageUrl = "http://example.com/image.jpg";
//
//        FeedResponseDto feed = FeedResponseDto.builder()
//                .feedId(feedId)
//                .user(FeedUserResponseDto.builder()
//                        .userId(userId)
//                        .nickname("test nickname")
//                        .profileImage(imageUrl)
//                        .build())
//                .contents(contents)
//                .imageUrl(imageUrl)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        FeedRequestDto requestDto = FeedRequestDto.builder()
//                .userId(userId)
//                .contents(contents)
//                .imageUrl(imageUrl)
//                .build();
//
//        when(feedService.saveFeed(any(FeedRequestDto.class))).thenReturn(feed);
//
//        // When & Then
//        mockMvc.perform(post("/feeds/write")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Feed created successfully"));
//
//        verify(feedService, times(1)).saveFeed(any(FeedRequestDto.class));
//    }
//
//    @Test
//    @DisplayName("GET /api/feeds - 피드 목록 조회 테스트")
//    void getFeeds_success() throws Exception {
//        // Given
//        List<FeedResponseDto> mockFeedList = new ArrayList<>();
//        mockFeedList.add(FeedResponseDto.builder()
//                .feedId(1L)
//                .contents("Feed 1")
//                .imageUrl("http://example.com/image1.jpg")
//                .build());
//        mockFeedList.add(FeedResponseDto.builder()
//                .feedId(2L)
//                .contents("Feed 2")
//                .imageUrl("http://example.com/image2.jpg")
//                .build());
//
//        when(feedService.findFeedList()).thenReturn(new ArrayList<>(mockFeedList));
//
//        // When & Then
//        mockMvc.perform(get("/api/feeds"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", is(2)))
//                .andExpect(jsonPath("$[0].feedId", is(1)))
//                .andExpect(jsonPath("$[0].contents", is("Feed 1")))
//                .andExpect(jsonPath("$[1].feedId", is(2)))
//                .andExpect(jsonPath("$[1].contents", is("Feed 2")));
//
//        verify(feedService, times(1)).findFeedList();
//    }
//
//    @Test
//    @DisplayName("DELETE /api/feeds/{feedId} - 피드 삭제 테스트")
//    void deleteFeed_success() throws Exception {
//        // Given
//        Long userId = 1L;
//        Long feedId = 1L;
//
//        when(feedService.deleteFeed(userId, feedId)).thenReturn(true);
//
//        // When & Then
//        mockMvc.perform(delete("/api/feeds/{feedId}", feedId)
//                        .param("userId", String.valueOf(userId)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Feed deleted successfully"));
//
//        verify(feedService, times(1)).deleteFeed(userId, feedId);
//    }
//
//    @Test
//    @DisplayName("DELETE /api/feeds/{feedId} - 삭제 시 존재하지 않는 피드 처리 테스트")
//    void deleteFeed_notFound() throws Exception {
//        // Given
//        Long userId = 1L;
//        Long feedId = 1L;
//
//        when(feedService.deleteFeed(userId, feedId)).thenThrow(new IllegalArgumentException("There's no such feedId"));
//
//        // When & Then
//        mockMvc.perform(delete("/api/feeds/{feedId}", feedId)
//                        .param("userId", String.valueOf(userId)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("There's no such feedId"));
//
//        verify(feedService, times(1)).deleteFeed(userId, feedId);
//    }
//}
