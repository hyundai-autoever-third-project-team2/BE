package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.StoryResponseDto;
import com.autoever.carstore.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> deleteFeed(@RequestBody Map<String, Long> request) {
        return ResponseEntity.ok(feedService.deleteFeed(request.get("feedId")));
    }

    @GetMapping("/list")
    public ResponseEntity<List<StoryResponseDto>> getFeedList(@RequestParam(value = "userId", required = false) Long userId) {
        return ResponseEntity.ok(feedService.findFeedList(userId));
    }
}
