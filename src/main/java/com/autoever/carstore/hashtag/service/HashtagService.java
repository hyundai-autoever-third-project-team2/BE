package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.hashtag.dto.HashtagResponseDto;

import java.util.List;

public interface HashtagService {
    public HashtagResponseDto saveHashtag(String hashtag);
    public List<HashtagResponseDto> getHashtagList(Long feedId);
}
