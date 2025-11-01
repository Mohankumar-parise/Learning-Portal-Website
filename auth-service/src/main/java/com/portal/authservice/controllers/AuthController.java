package com.portal.authservice.controllers;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.RegisterDto;
import com.portal.authservice.services.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService authUserService;

    // http://localhost:8081/api/v1/auth/register
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@Valid @RequestBody RegisterDto registerDto) {
        APIResponse<String> response = authUserService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
    // http://localhost:8081/api/v1/auth/register
    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> login(@Valid @RequestBody LoginDto loginDto) {
        APIResponse<String> response = authUserService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    // handle validation errors globally for this controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

}
