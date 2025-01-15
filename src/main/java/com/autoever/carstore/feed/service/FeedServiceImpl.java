package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dao.FeedLikeRepository;
import com.autoever.carstore.feed.dto.FeedMapper;
import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.dto.StoryResponseDto;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.service.FeedHashtagService;
import com.autoever.carstore.hashtag.service.HashtagService;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.s3.ImageUploadService;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;

    private final UserRepository userRepository;

    private final FeedMapper feedMapper;

    private final ImageUploadService imageUploadService;

    private final HashtagService hashtagService;

    private final FeedHashtagService feedHashtagService;

    private final FeedLikeRepository feedLikeRepository;

    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public void saveFeed(FeedRequestDto feedRequestDto) throws IOException {
        UserEntity user = securityUtil.getLoginUser();

        if(user == null){
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        String imageUrl = imageUploadService.upload(feedRequestDto.getImage());

        if(imageUrl == null){
            throw new IllegalArgumentException("imageUrl can't be null");
        }

        feedRequestDto.setUserId(user.getUserId());

        FeedEntity feed = feedMapper.dtoToEntity(feedRequestDto);
        feed.updateImageUrl(imageUrl);

        List<HashtagResponseDto> hashtagList = new ArrayList<>();

        FeedEntity savedFeed = feedRepository.save(feed);

        for(String hashtag : feedRequestDto.getHashtagList()){
            HashtagResponseDto hashtagDto = hashtagService.saveHashtag(hashtag);
            FeedHashTagResponseDto feedHashTagResponseDto = feedHashtagService.saveFeedHashtag(feed, HashtagEntity.builder()
                            .hashtagId(hashtagDto.getHashtagId())
                            .hashtag(hashtagDto.getHashtag())
                            .build());
            hashtagList.add(feedHashTagResponseDto.getHashTag());
        }
    }

    @Override
    @Transactional
    public Boolean deleteFeed(Long feedId) {
        if(feedId == null){
            throw new IllegalArgumentException("feedId can't be null");
        }

        FeedEntity feed = feedRepository.findById(feedId).orElse(null);

        UserEntity user = securityUtil.getLoginUser();

        if(user == null){
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        if(feed == null){
            throw new IllegalArgumentException("There's no such feedId");
        }

        if(feed.getUser().getUserId() != user.getUserId()){
            throw new IllegalArgumentException("다른 유저가 작성한 피드를 삭제할 수 없습니다.");
        }

        feedHashtagService.deleteFeedHashtag(feed);

        feed.updateIsDeleted();

        FeedEntity result = feedRepository.save(feed);

        return (result != null);
    }

    @Override
    public List<StoryResponseDto> findFeedList() {
        UserEntity currentUser = securityUtil.getLoginUser();

        if(currentUser == null){
            throw new IllegalArgumentException("로그인된 사용자가 없습니다.");
        }

        List<FeedEntity> feeds = feedRepository.findAllActiveFeeds();

        Map<Long, List<FeedEntity>> groupedFeeds = feeds.stream()
                .collect(Collectors.groupingBy(feed -> feed.getUser().getUserId()));

        return groupedFeeds.entrySet().stream()
                .map(entry -> {
                    UserEntity user = entry.getValue().get(0).getUser();
                    List<FeedResponseDto> feedDtos = entry.getValue().stream()
                            .map(feed -> FeedResponseDto.builder()
                                    .id(feed.getFeedId())
                                    .content(feed.getContents())
                                    .imageUrl(feed.getImageUrl())
                                    .isLiked(feedLikeRepository.existsByUserAndFeed(currentUser, feed))  // 로그인한 유저의 정보가 없으므로 기본값 false
                                    .createdAt(feed.getCreatedAt())
                                    .tags(feedHashtagService.getFeedHashtagList(feed.getFeedId())
                                            .stream()
                                            .map(HashtagResponseDto::getHashtag)
                                            .collect(Collectors.toList()))
                                    .build())
                            .collect(Collectors.toList());

                    return StoryResponseDto.builder()
                            .userId(user.getUserId())
                            .nickname(user.getNickname())
                            .profile(user.getProfileImage())
                            .stories(feedDtos)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
