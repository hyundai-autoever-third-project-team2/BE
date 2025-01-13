package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.repository.FeedHashtagRepository;
import com.autoever.carstore.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedHashtagServiceImpl implements FeedHashtagService {

    private final FeedHashtagRepository feedHashtagRepository;
    private final FeedRepository feedRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public FeedHashTagResponseDto saveFeedHashtag(Long feedId, Long hashtagId) {
        if(feedId == null) {
            throw new IllegalArgumentException("feedId cannot be null");
        }
        if(hashtagId == null) {
            throw new IllegalArgumentException("hashtagId cannot be null");
        }

        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("There's no such feedId"));

        HashtagEntity hashtag = hashtagRepository.findById(hashtagId)
                .orElseThrow(() -> new IllegalArgumentException("There's no such hashtagId"));

        FeedHashtagEntity feedHashtag = feedHashtagRepository.save(
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(hashtag)
                        .build()
        );

        return FeedHashTagResponseDto.builder()
                .feedId(feedHashtag.getFeed().getFeedId())
                .hashTag(feedHashtag.getHashtag().getHashtag())
                .build();
    }

    @Override
    public List<HashtagResponseDto> getFeedHashtagList(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("There's no such feedId"));

        return feedHashtagRepository.findByFeed(feed).stream()
                .map(feedHashtag -> HashtagResponseDto.builder()
                        .hashtagId(feedHashtag.getHashtag().getHashtagId())
                        .hashtag(feedHashtag.getHashtag().getHashtag())
                        .build())
                .collect(Collectors.toList());
    }
}