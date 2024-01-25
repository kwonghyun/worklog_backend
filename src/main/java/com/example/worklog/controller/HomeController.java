package com.example.worklog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Value("${publishedAt}")
    private String publishedAt;

    @GetMapping("/")
    public String home() {
        return "This version was published at: " + publishedAt;
    }
}
