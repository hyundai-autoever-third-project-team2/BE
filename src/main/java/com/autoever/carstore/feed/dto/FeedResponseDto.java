package com.autoever.carstore.feed.dto;

import lombok.*;

import java.time.LocalDateTime;

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
}
