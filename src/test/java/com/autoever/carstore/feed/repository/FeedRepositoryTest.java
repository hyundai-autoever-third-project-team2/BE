package com.autoever.carstore.feed.repository;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureTestEntityManager
public class FeedRepositoryTest {

    @Autowired
    private FeedRepository feedRepository;

    @Test
    @DisplayName("저장되어 있는 feedId 값을 통해 특정 feed를 찾을 수 있다.")
    void findById_success(){
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        FeedEntity feed = FeedEntity.builder()
                .user(user)
                .contents("contents")
                .isDeleted(false)
                .imageUrl("https://discordapp.com/channels/1281082727277531159/1282672672270192712/1328162430135566389")
                .build();

        FeedEntity savedFeed = feedRepository.save(feed);

        // When
        Optional<FeedEntity> result = feedRepository.findById(savedFeed.getFeedId());

        // Then
        assertThat(result.isPresent()).isTrue();
        FeedEntity foundFeed = result.get();
        assertThat(foundFeed)
                .isNotNull()
                .extracting(
                        FeedEntity::getUser,
                        FeedEntity::getContents,
                        FeedEntity::isDeleted,
                        FeedEntity::getImageUrl
                )
                .containsExactly(savedFeed.getUser(), savedFeed.getContents(), savedFeed.isDeleted(), savedFeed.getImageUrl());
    }

    @Test
    @DisplayName("저장되어 있지 않은 feedId 값을 통해 조회하면 null을 리턴한다.")
    void findById_success2(){
        // Given & When
        Optional<FeedEntity> result = feedRepository.findById(2L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유효한 데이터를 통해 저장을 시도하면 저장이 완료되야한다.")
    void save_withValidData_success() {
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        FeedEntity feed = FeedEntity.builder()
                .user(user)
                .contents("test contents")
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        // When
        FeedEntity savedFeed = feedRepository.save(feed);

        // Then
        assertThat(savedFeed)
                .isNotNull()
                .extracting(
                        FeedEntity::getUser,
                        FeedEntity::getContents,
                        FeedEntity::isDeleted,
                        FeedEntity::getImageUrl
                )
                .containsExactly(feed.getUser(), feed.getContents(), feed.isDeleted(), feed.getImageUrl());
    }

    @Test
    @DisplayName("contents 값이 null이어도 저장이 완료되야한다.")
    void save_withNullContents_success() {
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        FeedEntity feed = FeedEntity.builder()
                .user(user)
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        // When
        FeedEntity savedFeed = feedRepository.save(feed);

        // Then
        assertThat(savedFeed)
                .isNotNull()
                .extracting(
                        FeedEntity::getUser,
                        FeedEntity::getContents,
                        FeedEntity::isDeleted,
                        FeedEntity::getImageUrl
                )
                .containsExactly(feed.getUser(), feed.getContents(), feed.isDeleted(), feed.getImageUrl());
    }

    @Test
    @DisplayName("피드 리스트를 최신순으로 불러온다.(삭제된 피드는 조회하지 않는다.)")
    void findByIsDeletedFalseOrderByCreatedAtDesc_success() {
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        FeedEntity feed1 = FeedEntity.builder()
                .user(user)
                .contents("test contents1")
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        FeedEntity savedFeed1 = feedRepository.save(feed1);

        FeedEntity feed2 = FeedEntity.builder()
                .user(user)
                .isDeleted(true)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        FeedEntity savedFeed2 = feedRepository.save(feed2);

        FeedEntity feed3 = FeedEntity.builder()
                .user(user)
                .contents("test contents3")
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        FeedEntity savedFeed3 = feedRepository.save(feed3);

        FeedEntity feed4 = FeedEntity.builder()
                .user(user)
                .contents("test contents4")
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        FeedEntity savedFeed4 = feedRepository.save(feed4);

//        System.out.println(savedFeed1.getCreatedAt());
//        System.out.println(savedFeed3.getCreatedAt());
//        System.out.println(savedFeed4.getCreatedAt());

        // When
        List<FeedEntity> result = feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc();

//        System.out.println(result);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(savedFeed4);
        assertThat(result.get(1)).isEqualTo(savedFeed3);
        assertThat(result.get(2)).isEqualTo(savedFeed1);
    }

    @Test
    @DisplayName("특정 피드의 feedId를 통해 삭제하면 데이터베이스에서 해당 피드의 isDeleted 속성이 변경되어야 한다.")
    void save_success3() {
        // Given
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .nickname("test")
                .build();

        FeedEntity feed = FeedEntity.builder()
                .user(user)
                .isDeleted(false)
                .imageUrl("https://imgsc.chutcha.kr/files/car_resist/202412/29/2024122911065817354380182231_ori.jpg?s=1024x768&t=crop")
                .build();

        FeedEntity savedFeed = feedRepository.save(feed);

        savedFeed.updateIsDeleted();    // 삭제 상태로 변경 (isDeleted -> true)

        // When
        FeedEntity updatedFeed = feedRepository.save(savedFeed);

        // Then
        assertThat(updatedFeed.isDeleted()).isTrue();
    }
}
