package com.autoever.carstore.fcm.controller;

//import com.autoever.carstore.fcm.FCMUtil;
import com.autoever.carstore.fcm.dto.FCMRequestDto;
import com.autoever.carstore.fcm.service.FCMService;
import com.autoever.carstore.recommend.service.RecommendService;
import com.autoever.carstore.recommend.service.implement.RecommendServiceImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;
    private final RecommendService recommendService;

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

    @PutMapping("/test")
    public ResponseEntity<?> pushMessage() throws Exception {

        recommendService.updateRecommendations();

        return ResponseEntity.ok("fcm alarm success");
    }
}
