package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.dao.FeedHashtagRepository;
import com.autoever.carstore.hashtag.dao.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final FeedHashtagRepository feedHashtagRepository;
    private final FeedRepository feedRepository;

    @Override
    public HashtagResponseDto saveHashtag(String hashtag) {
        if (hashtag == null) {
            throw new IllegalArgumentException("hashtag can't be null");
        }

        HashtagEntity entity = hashtagRepository.save(
                HashtagEntity.builder()
                        .hashtag(hashtag)
                        .build()
        );

        return HashtagResponseDto.builder()
                .hashtagId(entity.getHashtagId())
                .hashtag(entity.getHashtag())
                .build();
    }

    @Override
    public HashtagResponseDto getHashtag(Long hashtagId) {
        HashtagEntity entity = hashtagRepository.findById(hashtagId).orElse(null);

        if (entity == null) {
            return null;
        }

        return HashtagResponseDto.builder()
                .hashtagId(entity.getHashtagId())
                .hashtag(entity.getHashtag())
                .build();
    }
}
