package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.repository.FeedHashtagRepository;
import com.autoever.carstore.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .hashtag(entity.getHashtag())
                .build();
    }

    @Override
    public List<HashtagResponseDto> getHashtagList(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("There's no hashtag on this feedId"));

        return feedHashtagRepository.findByFeed(feed).stream()
                .map(feedHashtag -> HashtagResponseDto.builder()
                        .hashtag(feedHashtag.getHashtag().getHashtag())
                        .build())
                .collect(Collectors.toList());
    }
}
