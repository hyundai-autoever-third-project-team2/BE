package com.autoever.carstore.admin.controller;

import com.autoever.carstore.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminService adminService;

    @PostMapping("/admin/judge/complete")
    public ResponseEntity<Void> completeJudge(@RequestBody Map<String, Object> request) {
        Long purchaseId = Long.parseLong(request.get("purchaseId").toString());
        int price = Integer.parseInt(request.get("price").toString());
        adminService.completeJudge(purchaseId, price);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/admin/judge/reject")
    public ResponseEntity<Void> rejectJudge(@RequestBody Map<String, Object> request) {
        Long purchaseId = Long.parseLong(request.get("purchaseId").toString());
        adminService.rejectJudge(purchaseId);
        return ResponseEntity.ok().build();
    }
}
