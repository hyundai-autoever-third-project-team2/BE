package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dao.FeedLikeRepository;
import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.entity.FeedLikeEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    @Override
    public void clickFeedLike(Long feedId) {
        UserEntity user = securityUtil.getLoginUser();

        if (user == null) {
            throw new IllegalArgumentException("로그인된 유저가 없습니다.");
        }

        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();

        feedLikeRepository.save(FeedLikeEntity.builder()
                .feed(feed)
                .user(user)
                .build());
    }

    @Override
    @Transactional
    public void unclickFeedLike(Long feedId) {
        UserEntity user = securityUtil.getLoginUser();

        if (user == null) {
            throw new IllegalArgumentException("로그인된 유저가 없습니다.");
        }

        FeedEntity feed = feedRepository.findById(feedId).orElseThrow();

        feedLikeRepository.deleteByUserAndFeed(user, feed);
    }
}
