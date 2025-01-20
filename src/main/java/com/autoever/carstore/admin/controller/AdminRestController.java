package com.autoever.carstore.admin.controller;

import com.autoever.carstore.admin.dto.request.RegistrationRequestDto;
import com.autoever.carstore.admin.service.AdminService;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminService adminService;
    private final UserService userService;

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

    @PostMapping("/admin/registration/submit")
    @ResponseBody
    public ResponseEntity<?> submitRegistration(@RequestBody RegistrationRequestDto requestDto) {
        adminService.submitRegistration(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/judge/images/{purchaseId}")
    public ResponseEntity<List<String>> getUserCarImages(@PathVariable Long purchaseId) {
        List<String> images = adminService.getImagesByPurchaseId(purchaseId);
        if (images == null || images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


}
