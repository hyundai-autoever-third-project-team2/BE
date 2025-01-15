package com.autoever.carstore.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNicknameRequestDto {
    private String nickname;
}
