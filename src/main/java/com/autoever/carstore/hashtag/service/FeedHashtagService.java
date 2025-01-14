package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.HashtagEntity;

import java.util.List;

public interface FeedHashtagService {
    public FeedHashTagResponseDto saveFeedHashtag(FeedEntity feed, HashtagEntity hashtag);
    public List<HashtagResponseDto> getFeedHashtagList(Long feedId);
    public void deleteFeedHashtag(FeedEntity feed);
}
