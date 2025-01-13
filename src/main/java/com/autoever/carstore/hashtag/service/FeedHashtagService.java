package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;

import java.util.List;

public interface FeedHashtagService {
    public FeedHashTagResponseDto saveFeedHashtag(Long feedId, Long hashtagId);
    public List<HashtagResponseDto> getFeedHashtagList(Long feedId);
}
