package com.autoever.carstore.feed.service;

public interface FeedLikeService {
    void clickFeedLike(Long feedId);

    void unclickFeedLike(Long feedId);
}
