package com.autoever.carstore.hashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedHashTagResponseDto {
    private Long feedId;
    private String hashTag;
}
