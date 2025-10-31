package com.portal.authservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    // http://localhost:7070/api/v1/admin/panel
    @GetMapping("/panel")
    public String getPanel() {
        return "Welcome Admin Panel";
    }
}
