package com.autoever.carstore.fcm.controller;

//import com.autoever.carstore.fcm.FCMUtil;
import com.autoever.carstore.fcm.dto.FCMRequestDto;
import com.autoever.carstore.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> pushMessage(@RequestBody FCMRequestDto requestDTO) throws Exception {
        System.out.println(requestDTO.getDeviceToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());
        fcmService.sendMessageTo(
                requestDTO.getDeviceToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok("fcm alarm success");
    }
}
