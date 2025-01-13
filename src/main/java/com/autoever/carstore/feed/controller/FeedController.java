package com.autoever.carstore.feed.controller;

import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.service.FeedService;
import com.autoever.carstore.s3.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> writeFeed(@RequestParam("userId") Long userId,
                                       @RequestParam(value = "contents", required = false) String contents,
                                       @RequestParam(value = "image") MultipartFile image) {
        try {
            String imageUrl = imageUploadService.upload(image);
            FeedRequestDto requestDto = FeedRequestDto.builder()
                    .userId(userId)
                    .contents(contents)
                    .imageUrl(imageUrl)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(feedService.saveFeed(requestDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Image upload failed");
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteFeed(@RequestParam("userId") Long userId,
                                        @RequestParam("feedId") Long feedId) {
        try {
            return ResponseEntity.ok(feedService.deleteFeed(userId, feedId));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("There's no such feedId")) {
                return ResponseEntity.notFound().build();
            }
            if (e.getMessage().equals("It's not this user's feed")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FeedResponseDto>> getFeedList() {
        return ResponseEntity.ok(feedService.findFeedList());
    }
}
