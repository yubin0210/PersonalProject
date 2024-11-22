package com.yubin.SpringBootTest.controller;

import com.yubin.SpringBootTest.service.UserService;
import com.yubin.SpringBootTest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String signupForm() {
        return "register";
    }

    @PostMapping("/register")
    public String signup(@ModelAttribute User user) {
        try {
            System.out.println("회원가입 요청: " + user.getUsername());
            userService.saveUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("회원가입 오류: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

}
