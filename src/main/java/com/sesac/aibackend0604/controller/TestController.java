package com.sesac.aibackend0604.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("CLIENT_ID = " + System.getenv("GOOGLE_CLIENT_ID"));
        return "ok";
    }
}