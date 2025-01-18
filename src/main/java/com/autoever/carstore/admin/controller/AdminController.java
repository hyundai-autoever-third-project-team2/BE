package com.autoever.carstore.admin.controller;


import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.admin.service.AdminService;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.dto.response.TransactionsResponseDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    // 메인 페이지 (유저 리스트 포함)
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        String adminName = userService.getUserName();
        model.addAttribute("adminNickname", adminName);

        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/home";
    }

    // 유저 리스트
    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // 유저 한 명 조회 (AJAX 요청에 사용)
    @GetMapping("/admin/users/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserEntity user = userService.getUserById(id);
        model.addAttribute("user", user); // "user"라는 이름으로 데이터 전달
        return "admin/userDetail";
    }

    @GetMapping("/admin/judge")
    public String judge(Model model) {
        model.addAttribute("currentProgress", "before"); // 초기 상태
        model.addAttribute("judgeCars", adminService.getCarsByProgress("before"));
        return "admin/judge";
    }

    @GetMapping("/admin/judge/{progress}")
    public String filterByProgress(@PathVariable String progress, Model model) {
        model.addAttribute("currentProgress", progress);
        model.addAttribute("judgeCars", adminService.getCarsByProgress(progress));
        return "admin/judge";
    }

//    @PostMapping("/admin/judge/complete")
//    public ResponseEntity<Void> completeJudge(@RequestBody Map<String, Object> request) {
//        Long purchaseId = Long.parseLong(request.get("purchaseId").toString());
//        int price = Integer.parseInt(request.get("price").toString());
//        adminService.completeJudge(purchaseId, price);
//        return ResponseEntity.ok().build();
//    }
//    @PostMapping("/admin/judge/reject")
//    public ResponseEntity<Void> rejectJudge(@RequestBody Map<String, Object> request) {
//        Long purchaseId = Long.parseLong(request.get("purchaseId").toString());
//        adminService.rejectJudge(purchaseId);
//        return ResponseEntity.ok().build();
//    }


}
