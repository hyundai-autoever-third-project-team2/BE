package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dao.FeedLikeRepository;
import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.entity.FeedLikeEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeServiceImpl implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Override
    public void clickFeedLike(Long userId, Long feedId) {

        UserEntity user = userRepository.findById(userId).orElseThrow();
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();

        feedLikeRepository.save(FeedLikeEntity.builder()
                .feed(feed)
                .user(user)
                .build());
    }

    @Override
    @Transactional
    public void unclickFeedLike(Long userId, Long feedId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();

        feedLikeRepository.deleteByUserAndFeed(user, feed);
    }
}
