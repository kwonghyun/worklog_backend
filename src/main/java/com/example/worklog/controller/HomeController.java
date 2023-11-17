package com.example.worklog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Value("${version}")
    private String version;

    @GetMapping("/")
    public String home() {
        return "Hello CICD! version: " + version;
    }
}
