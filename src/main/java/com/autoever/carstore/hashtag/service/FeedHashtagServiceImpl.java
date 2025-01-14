package com.autoever.carstore.hashtag.service;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.dao.FeedHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedHashtagServiceImpl implements FeedHashtagService {

    private final FeedHashtagRepository feedHashtagRepository;
    private final FeedRepository feedRepository;

    @Override
    public FeedHashTagResponseDto saveFeedHashtag(FeedEntity feed, HashtagEntity hashtag) {
        if(feed == null) {
            throw new IllegalArgumentException("feed cannot be null");
        }
        if(hashtag == null) {
            throw new IllegalArgumentException("hashtag cannot be null");
        }

        FeedHashtagEntity feedHashtag = feedHashtagRepository.save(
                FeedHashtagEntity.builder()
                        .feed(feed)
                        .hashtag(hashtag)
                        .build()
        );

        return FeedHashTagResponseDto.builder()
                .feedId(feedHashtag.getFeed().getFeedId())
                .hashTag(HashtagResponseDto.builder()
                        .hashtagId(hashtag.getHashtagId())
                        .hashtag(hashtag.getHashtag())
                        .build())
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

    @Override
    public void deleteFeedHashtag(FeedEntity feed) {
        feedHashtagRepository.deleteByFeed(feed);
    }
}