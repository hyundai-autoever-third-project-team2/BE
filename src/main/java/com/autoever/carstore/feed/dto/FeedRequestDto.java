package com.autoever.carstore.feed.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequestDto {
    private Long userId;
    private String contents;
    private String imageUrl;
    private List<String> hashtagList;
}
