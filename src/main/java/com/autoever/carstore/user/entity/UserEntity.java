package com.autoever.carstore.user.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean survey;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "refresh_token", unique = true)
    private String refreshToken;

    @Column(name = "is_active")
    private boolean isActive;   // 정지 여부

    @Column(name = "fcm_token", unique = true)
    private String fcmToken;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    public void updateSurvey(){
        this.survey = true;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void updateIsActive(){
        this.isActive = !this.isActive;
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void updateUserRole(String userRole){
        this.userRole = userRole;
    }
}
