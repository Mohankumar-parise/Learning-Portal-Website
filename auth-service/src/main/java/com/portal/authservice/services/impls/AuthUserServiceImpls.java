package com.portal.authservice.services.impls;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.LoginResponse;
import com.portal.authservice.dtos.RegisterDto;
import com.portal.authservice.models.AuthUser;
import com.portal.authservice.repositories.AuthUserRepository;
import com.portal.authservice.services.AuthUserService;
import com.portal.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpls implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public APIResponse<String> register(RegisterDto registerDto) {
        APIResponse<String> response = new APIResponse<>();
        // check user is exists by username
        if (authUserRepository.existsByUsername(registerDto.getUsername())) {
            response.setMessage("Registration Failed");
            response.setStatusCode(409);
            response.setData("Username is already Exists");
            return response;
        }
        // check user is exists by email
        if (authUserRepository.existsByEmail(registerDto.getEmail())) {
            response.setMessage("Registration Failed");
            response.setStatusCode(409);
            response.setData("Email is already exists");
            return response;
        }
        // encode password and set role_
        AuthUser authUser = modelMapper.map(registerDto, AuthUser.class);
        authUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        authUser.setRole("ROLE_" + registerDto.getRole().toUpperCase());
        // save to db and send backs the response
        authUserRepository.save(authUser);
        response.setMessage("Registration Successful");
        response.setStatusCode(201);
        response.setData("New User is Created");
        return response;
    }

    @Override
    public LoginResponse<String> login(LoginDto loginDto) {
        LoginResponse<String> response = new LoginResponse<String>();
        // Get username and password
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);
        System.out.println("Authentication Object: " + authentication.isAuthenticated());
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.generateJwtToken(
                    loginDto.getUsername(), authentication.getAuthorities().iterator().next().getAuthority());

            response.setMessage("Login Successful");
            response.setStatusCode(200);
            response.setData(jwtToken);
//            AuthUser authUser = authUserRepository.findByUsername(loginDto.getUsername());
            response.setRole(authUserRepository.findByUsername(loginDto.getUsername()).getRole());
            log.info("{}", response);
//            return response;
        } else {
            response.setMessage("Login Failed");
            response.setStatusCode(409);
            response.setData("Something went wrong");
            log.info("{}", response);
            return response;
        }
        return response;
    }
    public String getRole(String username) {
        AuthUser authUser = authUserRepository.findByUsername(username);
        if (authUser != null) {
            return authUser.getRole();
        }
        return null; // or throw an exception if user not found
    }
}
