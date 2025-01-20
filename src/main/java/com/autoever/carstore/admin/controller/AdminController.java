package com.autoever.carstore.admin.controller;


import com.autoever.carstore.admin.dto.response.AgencyDto;
import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.admin.dto.response.RegistrationResponseDto;
import com.autoever.carstore.admin.service.AdminService;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.dto.response.TransactionsResponseDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @ModelAttribute("agencies")
    public List<AgencyDto> getAgencies() {
        return adminService.getAllAgencies();
    }

    // 메인 페이지
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        String adminName = userService.getUserName();
        model.addAttribute("adminNickname", adminName);
        return "admin/home";
    }


        // 유저 리스트
    @GetMapping("/admin/users")
    public String getAllUsers(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = userService.getAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/users";
    }


    @GetMapping("/admin/judge")
    public String judge(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("currentProgress", "before"); // 초기 상태
        model.addAttribute("judgeCars", adminService.getCarsByProgress("before", pageable));
        return "admin/judge";
    }

    @GetMapping("/admin/judge/{progress}")
    public String filterByProgress(@PathVariable String progress, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("currentProgress", progress);
        model.addAttribute("judgeCars", adminService.getCarsByProgress(progress, pageable));
        return "admin/judge";
    }

    @GetMapping("/admin/registration")
    public String registration(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("currentProgress", false); // 초기 상태
        model.addAttribute("registrationCars", adminService.getRegistrationCarsByProgress(false, pageable));
        return "admin/registration";
    }

    @GetMapping("/admin/registration/{isVisible}")
    public String registration(@PathVariable boolean isVisible, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("currentProgress", isVisible);
        model.addAttribute("registrationCars", adminService.getRegistrationCarsByProgress(isVisible, pageable));
        return "admin/registration";
    }

    @GetMapping("/admin/chat")
    public String chat() {
        return "admin/chat";
    }
}
