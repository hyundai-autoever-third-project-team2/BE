package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;

import java.io.IOException;
import java.util.List;

public interface FeedService {
    public FeedResponseDto saveFeed(FeedRequestDto feedRequestDto) throws IOException;
    public Boolean deleteFeed(Long userId, Long feedId);
    public List<FeedResponseDto> findFeedList();
}
