package com.autoever.carstore.hashtag.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponseDto {
    private Long hashtagId;
    private String hashtag;
}
