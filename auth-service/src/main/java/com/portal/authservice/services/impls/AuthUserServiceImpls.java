package com.portal.authservice.services.impls;

import com.portal.authservice.dtos.APIResponse;
import com.portal.authservice.dtos.LoginDto;
import com.portal.authservice.dtos.RegisterDto;
import com.portal.authservice.models.AuthUser;
import com.portal.authservice.repositories.AuthUserRepository;
import com.portal.authservice.services.AuthUserService;
import com.portal.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

        try {
            // Check if user already exists by username
            if (authUserRepository.existsByUsername(registerDto.getUsername())) {
                response.setMessage("Registration Failed");
                response.setStatusCode(409);
                response.setData("Username already exists");
                return response;
            }

            // Check if user already exists by email
            if (authUserRepository.existsByEmail(registerDto.getEmail())) {
                response.setMessage("Registration Failed");
                response.setStatusCode(409);
                response.setData("Email already exists");
                return response;
            }

            // Map RegisterDto to AuthUser entity
            AuthUser authUser = modelMapper.map(registerDto, AuthUser.class);

            // Encode password and set role
            authUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            authUser.setRole("ROLE_" + registerDto.getRole().toUpperCase());

            // Save to database
            authUserRepository.save(authUser);

            // Success response
            response.setMessage("Registration Successful");
            response.setStatusCode(201);
            response.setData("New User is Created");
            return response;

        } catch (IllegalArgumentException e) {
            // This can occur if mapper or encoder gets invalid input
            response.setMessage("Registration Failed");
            response.setStatusCode(400);
            response.setData("Invalid input data provided");
            log.error("IllegalArgumentException during registration: {}", e.getMessage());
            return response;

        } catch (DataIntegrityViolationException e) {
            // This handles DB constraint violations (duplicate keys, etc.)
            response.setMessage("Registration Failed");
            response.setStatusCode(409);
            response.setData("User with given credentials already exists");
            log.error("Database constraint error: {}", e.getMessage());
            return response;

        } catch (Exception e) {
            // Catch-all for any other unexpected exceptions
            response.setMessage("Registration Failed");
            response.setStatusCode(500);
            response.setData("Something went wrong during registration");
            log.error("Unexpected error during registration: {}", e.getMessage(), e);
            return response;
        }
    }


    @Override
    public APIResponse<String> login(LoginDto loginDto) {
        APIResponse<String> response = new APIResponse<>();

        try {
            // Create authentication token
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(), loginDto.getPassword());

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(token);

            if (authentication.isAuthenticated()) {
                // Generate JWT if authenticated
                String jwtToken = jwtService.generateJwtToken(
                        loginDto.getUsername(),
                        authentication.getAuthorities().iterator().next().getAuthority()
                );
                response.setMessage("Login Successful");
                response.setStatusCode(200);
                response.setData(jwtToken);
                log.info("{}", response);
                return response;
            } else {
                response.setMessage("Login Failed");
                response.setStatusCode(409);
                response.setData("Something went wrong");
                log.error("Authentication failed for user: {}", loginDto.getUsername());
                return response;
            }
        } catch (BadCredentialsException e) {
            // Handle invalid username/password
            response.setMessage("Login Failed");
            response.setStatusCode(409);
            response.setData("Invalid username or password");
            log.error("Authentication failed: {}", e.getMessage());
            return response;

        } catch (Exception e) {
            // Handle other unexpected exceptions
            response.setMessage("Login Failed");
            response.setStatusCode(500);
            response.setData("Something went wrong");
            log.error("Unexpected error during login: {}", e.getMessage());
            return response;
        }
    }
}
