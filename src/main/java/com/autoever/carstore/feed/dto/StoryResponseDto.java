package com.autoever.carstore.feed.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryResponseDto {
    private Long userId;
    private String nickname;
    private String profile;
    private List<FeedResponseDto> stories;
}
