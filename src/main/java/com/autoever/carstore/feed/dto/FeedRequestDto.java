package com.autoever.carstore.feed.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequestDto {
    private Long userId;
    private String contents;
    private String imageUrl;
}
