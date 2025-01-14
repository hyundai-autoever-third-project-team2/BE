package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.hashtag.dto.HashtagResponseDto;

public interface HashtagService {
    public HashtagResponseDto saveHashtag(String hashtag);
    public HashtagResponseDto getHashtag(Long hashtagId);
}
