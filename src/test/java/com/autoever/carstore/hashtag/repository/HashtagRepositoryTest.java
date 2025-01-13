package com.autoever.carstore.hashtag.repository;

import com.autoever.carstore.hashtag.entity.HashtagEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureTestEntityManager
public class HashtagRepositoryTest {

    @Autowired
    private HashtagRepository hashtagRepository;

    @Test
    @DisplayName("유효한 데이터를 통해 저장을 시도하면 데이터베이스에 저장되어야 한다.")
    void save_withValidData_success(){
        // Given
        String hashtag = "test";

        HashtagEntity hashtagEntity = HashtagEntity.builder()
                .hashtag(hashtag)
                .build();

        // When
        HashtagEntity savedHashtag = hashtagRepository.save(hashtagEntity);

        // Then
        assertThat(savedHashtag).isNotNull();
        assertEquals(savedHashtag.getHashtag(), hashtag);
    }

    @Test
    @DisplayName("hashtag 값으로 조회하면 해당하는 HashtagEntity가 반환된다.")
    void findByHashtag_withValidHashtag_returnHashtagEntity(){
        // Given
        String hashtag = "test";

        HashtagEntity hashtagEntity = HashtagEntity.builder()
                .hashtag(hashtag)
                .build();

        hashtagRepository.save(hashtagEntity);

        // When
        Optional<HashtagEntity> result = hashtagRepository.findByHashtag(hashtag);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertEquals(result.get().getHashtag(), hashtag);
    }

    @Test
    @DisplayName("존재하지 않는 hashtag 값으로 조회하면 해당하는 Null가 반환된다.")
    void findByHashtag_withNotExistHashtag_returnNull(){
        // Given
        String hashtag = "test";

        // When
        Optional<HashtagEntity> result = hashtagRepository.findByHashtag(hashtag);

        // Then
        assertThat(result).isEmpty();
    }
}
