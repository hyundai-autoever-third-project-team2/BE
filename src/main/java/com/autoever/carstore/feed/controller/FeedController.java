package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.StoryResponseDto;
import com.autoever.carstore.feed.service.FeedService;
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

    @PostMapping("/write")
    public ResponseEntity<String> writeFeed(@RequestBody FeedRequestDto feedRequestDto) throws IOException {
        feedService.saveFeed(feedRequestDto);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteFeed(@RequestParam("feedId") Long feedId) {
        return ResponseEntity.ok(feedService.deleteFeed(feedId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<StoryResponseDto>> getFeedList() {
        return ResponseEntity.ok(feedService.findFeedList());
    }
}
