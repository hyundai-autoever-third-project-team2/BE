package com.autoever.carstore.fcm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private String resultCode;
    private String message;
    private T data;
}
