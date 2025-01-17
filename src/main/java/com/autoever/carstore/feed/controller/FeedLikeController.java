package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedLike")
@RequiredArgsConstructor
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    @PostMapping("/click")
    public ResponseEntity<?> clickFeedLike(@RequestParam Long feedId){

        feedLikeService.clickFeedLike(feedId);

        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }

    @DeleteMapping("/unclick")
    public ResponseEntity<?> unclickFeedLike(@RequestParam Long feedId){

        feedLikeService.unclickFeedLike(feedId);

        return ResponseEntity.ok("success");
    }

}
