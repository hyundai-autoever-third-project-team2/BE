package com.autoever.carstore.test.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginSuccessController {

    @GetMapping("/my-success")
    public String handleLoginSuccess(@RequestParam String accessToken, @RequestParam String refreshToken, @RequestParam String role) {
        return accessToken + " : " + refreshToken + " : " + role;
    }

}
