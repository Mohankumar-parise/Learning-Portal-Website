package com.portal.authservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
public class StudentDashboard {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to Student Dashboard";
    }
    // today's topic
    @GetMapping("/todays-topic")
    public String todaysTopic() {
        return "Today's Topic: Advanced Java Programming";
    }
    // task manager
    @GetMapping("/task-manager")
    public String taskManager() {
        return "Task Manager: You have 3 pending tasks.";
    }
    // exam section
    @GetMapping("/exam-section")
    public String examSection() {
        return "Exam Section: Next exam on 25th June.";
    }
    // progress view
    @GetMapping("/progress-view")
    public String progressView() {
        return "Progress View: You have completed 75% of the course.";
    }
}
