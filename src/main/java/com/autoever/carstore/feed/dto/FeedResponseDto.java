package com.autoever.carstore.feed.dto;

import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponseDto {
    private long feedId;
    private FeedUserResponseDto user;
    private String contents;
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<HashtagResponseDto> hashtagList;
}
