package com.portal.authservice.services;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.RegisterDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthUserService {

    APIResponse<String> register(RegisterDto registerDto);
    APIResponse<String> login(LoginDto loginDto);
}
