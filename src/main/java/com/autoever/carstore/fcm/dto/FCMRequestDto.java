package com.autoever.carstore.fcm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FCMRequestDto {
    private String deviceToken;

    private String title;

    private String body;
}
