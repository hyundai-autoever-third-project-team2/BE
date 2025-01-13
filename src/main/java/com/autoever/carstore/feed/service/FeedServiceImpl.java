package com.autoever.carstore.feed.service;

import com.autoever.carstore.feed.dto.FeedMapper;
import com.autoever.carstore.feed.dto.FeedRequestDto;
import com.autoever.carstore.feed.dto.FeedResponseDto;
import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.repository.FeedRepository;
import com.autoever.carstore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedMapper feedMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Override
    public FeedResponseDto saveFeed(FeedRequestDto feedRequestDto) {
        if(feedRequestDto.getUserId() == null){
            throw new IllegalArgumentException("user can't be null");
        }
        if(feedRequestDto.getImageUrl() == null){
            throw new IllegalArgumentException("imageUrl can't be null");
        }

        FeedEntity feed = feedMapper.dtoToEntity(feedRequestDto);

        FeedEntity result = feedRepository.save(feed);

        return feedMapper.entityToDto(result);
    }

    @Override
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

        feed.updateIsDeleted();

        FeedEntity result = feedRepository.save(feed);

        return (result != null);
    }

    @Override
    public List<FeedResponseDto> findFeedList() {
        return feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc().stream()
                .map(feedMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
