package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dto.FeedMapper;
import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.dao.FeedRepository;
import com.autoever.carstore.hashtag.dto.FeedHashTagResponseDto;
import com.autoever.carstore.hashtag.dto.HashtagResponseDto;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import com.autoever.carstore.hashtag.service.FeedHashtagService;
import com.autoever.carstore.hashtag.service.HashtagService;
import com.autoever.carstore.s3.ImageUploadService;
import com.autoever.carstore.user.dao.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    @Transactional
    public FeedResponseDto saveFeed(FeedRequestDto feedRequestDto) throws IOException {
        if(feedRequestDto.getUserId() == null){
            throw new IllegalArgumentException("user can't be null");
        }

        if(userRepository.findById(feedRequestDto.getUserId()).isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다");
        }

        String imageUrl = imageUploadService.upload(feedRequestDto.getImage());

        if(imageUrl == null){
            throw new IllegalArgumentException("imageUrl can't be null");
        }

        FeedEntity feed = feedMapper.dtoToEntity(feedRequestDto);
        feed.updateImageUrl(imageUrl);

        List<HashtagResponseDto> hashtagList = new ArrayList<>();

        FeedEntity savedFeed = feedRepository.save(feed);

        FeedResponseDto result = feedMapper.entityToDto(savedFeed);

        for(String hashtag : feedRequestDto.getHashtagList()){
            HashtagResponseDto hashtagDto = hashtagService.saveHashtag(hashtag);
            FeedHashTagResponseDto feedHashTagResponseDto = feedHashtagService.saveFeedHashtag(feed, HashtagEntity.builder()
                            .hashtagId(hashtagDto.getHashtagId())
                            .hashtag(hashtagDto.getHashtag())
                            .build());
            hashtagList.add(feedHashTagResponseDto.getHashTag());
        }

        result.setHashtagList(hashtagList);

        return result;
    }

    @Override
    @Transactional
    public Boolean deleteFeed(Long userId, Long feedId) {
        if(feedId == null){
            throw new IllegalArgumentException("feedId can't be null");
        }
        if(userId == null){
            throw new IllegalArgumentException("userId can't be null");
        }

        FeedEntity feed = feedRepository.findById(feedId).orElse(null);

        if(feed == null){
            throw new IllegalArgumentException("There's no such feedId");
        }
        if(feed.getUser().getUserId() != userId){
            throw new IllegalArgumentException("It's not this user's feed");
        }

        feedHashtagService.deleteFeedHashtag(feed);

        feed.updateIsDeleted();

        FeedEntity result = feedRepository.save(feed);

        return (result != null);
    }

    @Override
    public List<FeedResponseDto> findFeedList() {
        List<FeedResponseDto> result = feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc().stream()
                .map(feedMapper::entityToDto)
                .collect(Collectors.toList());

        for(FeedResponseDto response : result){
            List<HashtagResponseDto> li = feedHashtagService.getFeedHashtagList(response.getFeedId());
            response.setHashtagList(li);
        }

        return result;
    }
}
