package com.autoever.carstore.feed.dto;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {

    @Autowired
    private UserRepository userRepository;

    public FeedEntity dtoToEntity(FeedRequestDto dto) {

        return FeedEntity.builder()
                .user(userRepository.findById(dto.getUserId()).get())
                .contents(dto.getContents())
                .isDeleted(false)
                .imageUrl(dto.getImageUrl())
                .build();
    }

    public FeedResponseDto entityToDto(FeedEntity entity) {
        UserEntity user = entity.getUser();

        return FeedResponseDto.builder()
                        .user(FeedUserResponseDto.builder()
                                .userId(user.getUserId())
                                .nickname(user.getNickname())
                                .profileImage(entity.getImageUrl())
                                .build()
                        )
                .feedId(entity.getFeedId())
                .contents(entity.getContents())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
