package com.autoever.carstore.feed.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedUserResponseDto {
    private long userId;
    private String nickname;
    private String profileImage;
}
