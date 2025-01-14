package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.service.FeedService;
import com.autoever.carstore.s3.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController("/")
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    private final ImageUploadService imageUploadService;

    @PostMapping("/write")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> writeFeed(@ModelAttribute FeedRequestDto feedRequestDto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedService.saveFeed(feedRequestDto));
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteFeed(@RequestParam("userId") Long userId,
                                        @RequestParam("feedId") Long feedId) {
        return ResponseEntity.ok(feedService.deleteFeed(userId, feedId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FeedResponseDto>> getFeedList() {
        return ResponseEntity.ok(feedService.findFeedList());
    }
}
