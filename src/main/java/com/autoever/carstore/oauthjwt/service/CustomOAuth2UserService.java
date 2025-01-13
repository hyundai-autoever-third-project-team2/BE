package com.autoever.carstore.oauthjwt.service;

import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
import com.autoever.carstore.oauthjwt.dto.KakaoResponse;
import com.autoever.carstore.oauthjwt.dto.OAuth2Response;
import com.autoever.carstore.oauthjwt.dto.UserDTO;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }

        else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {

            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName())
                    .profileImage(oAuth2Response.getProfileImage() != null ? oAuth2Response.getProfileImage() : "")
                    .userRole("ROLE_USER")
                    .survey(false) // 설문 기본값
                    .isActive(true) // 활성화 상태
                    .build();


            userRepository.save(userEntity);


        }
        // 이미 정보가 존재하면 업데이트 처리
        else {
            existData.updateNickname(oAuth2Response.getName());
            existData.updateEmail(oAuth2Response.getEmail());
            existData.updateProfileImage(oAuth2Response.getProfileImage() != null ? oAuth2Response.getProfileImage() : "");

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setProfileImage(oAuth2Response.getProfileImage());
            userDTO.setRole(existData.getUserRole());

            return new CustomOAuth2User(userDTO);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(oAuth2Response.getEmail());
        userDTO.setName(oAuth2Response.getName());
        userDTO.setProfileImage(oAuth2Response.getProfileImage());
        userDTO.setRole("ROLE_USER");

        // CustomOAuth2User 반환
        return new CustomOAuth2User(userDTO);
    }
}