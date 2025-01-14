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
    private Long id;    // feedId
    private String content;
    private String imageUrl;
    private Boolean isLiked;
    private LocalDateTime createdAt;
    private List<String> tags;
}
