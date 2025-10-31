package com.portal.authservice.controllers;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.LoginResponse;
import com.portal.authservice.dtos.RegisterDto;
import com.portal.authservice.services.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthUserService authUserService;

    // http://localhost:8081/api/v1/auth/register
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<APIResponse<String>> register(@RequestBody RegisterDto registerDto) {
        APIResponse<String> response = authUserService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
    // http://localhost:7070/api/v1/auth/register
    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto loginDto) {
//        APIResponse<String> response = authUserService.login(loginDto);
//        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
//    }
        LoginResponse<String> response = authUserService.login(loginDto);
        System.out.println(response);
        if (response.getStatusCode() == 200) {
            // Extract the user's role (you should already get it in your AuthUserService)
            String role = response.getRole(); // You’ll see below how to handle this properly

            // Determine redirect URL based on role
            String redirectUrl = switch (role.toUpperCase()) {
                case "STUDENT" -> "/api/v1/student/dashboard";
                case "INSTRUCTOR" -> "/api/v1/instructor/dashboard";
                default -> "/api/v1/auth/login";
            };

            // Return both token and redirect URL
            APIResponse<String> redirectResponse = new APIResponse<>();
            redirectResponse.setMessage("Login successful. Redirecting to " + redirectUrl);
            redirectResponse.setStatusCode(200);
            redirectResponse.setData(response.getData());

            // Add redirect URL as a header (optional)
            return ResponseEntity.status(200)
                    .header("Location", redirectUrl)
                    .body(redirectResponse);
        } else {
            // Return the original response in case of failure
            APIResponse<String> errorResponse = new APIResponse<>();
            errorResponse.setMessage(response.getMessage());
            errorResponse.setStatusCode(response.getStatusCode());
            errorResponse.setData(null);
            return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(response.getStatusCode()));
        }

//        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
}
