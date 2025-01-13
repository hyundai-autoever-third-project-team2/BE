package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.entity.FeedEntity;

import java.util.ArrayList;
import java.util.List;

public interface FeedService {
    public FeedResponseDto saveFeed(FeedRequestDto feedRequestDto);
    public Boolean deleteFeed(Long userId, Long feedId);
    public List<FeedResponseDto> findFeedList();
}
