package com.portal.authservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/instructor")
public class InstructorDashboard {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to Instructor Dashboard";
    }
    // topic creation
    @GetMapping("/create-topic")
    public String createTopic() {
        return "Topic created successfully";
    }

    // Task assignment
    @GetMapping("/assign-task")
    public String assignTask() {
        return "Task assigned successfully";
    }

    // create / manage exams
    @GetMapping("/manage-exams")
    public String manageExams() {
        return "Exam managed successfully";
    }

}
