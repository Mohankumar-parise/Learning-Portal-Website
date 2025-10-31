package com.portal.authservice.controllers;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.RegisterDto;
import com.portal.authservice.services.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService authUserService;

    // http://localhost:8081/api/v1/auth/register
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<APIResponse<String>> register(@RequestBody RegisterDto registerDto) {
        APIResponse<String> response = authUserService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
    // http://localhost:8081/api/v1/auth/register
    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto loginDto) {
        APIResponse<String> response = authUserService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
