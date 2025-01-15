package com.autoever.carstore.feed.service;

public interface FeedLikeService {
    void clickFeedLike(Long userId, Long feedId);

    void unclickFeedLike(Long userId, Long feedId);
}
