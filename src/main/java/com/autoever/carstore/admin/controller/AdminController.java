package com.autoever.carstore.admin.controller;


import com.autoever.carstore.admin.service.AdminService;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // 계정 차단 or 활성화
    @PostMapping("/admin/users/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<String> toggleUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        boolean isActive = payload.get("isActive");
        try {
            adminService.toggleUserStatus(id, isActive); // Service로 로직 위임
            return ResponseEntity.ok("User status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user status");
        }
    }

}
