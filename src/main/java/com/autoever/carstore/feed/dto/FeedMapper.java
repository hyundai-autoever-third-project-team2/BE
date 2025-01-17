//package com.autoever.carstore.feed.dto;
//
//import com.autoever.carstore.feed.entity.FeedEntity;
//import com.autoever.carstore.user.entity.UserEntity;
//import com.autoever.carstore.user.dao.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FeedMapper {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public FeedEntity dtoToEntity(FeedRequestDto dto) {
//
//        return FeedEntity.builder()
//                .user(userRepository.findById(dto.getUserId()).orElse(null))
//                .contents(dto.getContents())
//                .isDeleted(false)
//                .build();
//    }
//}
