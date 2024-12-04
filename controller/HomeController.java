package com.yubin.SpringBootTest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/recommend")
    public String recommend() {
        return "recommend";
    }

    @GetMapping("/header")
    public String getHeader() {
        return "header";
    }

    @GetMapping("/footer")
    public String getFooter() {
        return "footer";
    }


}
