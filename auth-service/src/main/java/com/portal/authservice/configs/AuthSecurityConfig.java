package com.portal.authservice.configs;

import com.portal.authservice.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthSecurityConfig {

    private final AuthBeanConfig authBeanConfig;
    private final JwtFilter jwtFilter;

    // Publicly accessible endpoints
    private static final String[] OPEN_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    // Instructor-specific protected endpoints
    private static final String[] INSTRUCTOR_URLS = {
            "/api/v1/instructor/dashboard"
    };

    // Student-specific protected endpoints
    private static final String[] STUDENT_URLS = {
            "/api/v1/student/dashboard"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(OPEN_URLS).permitAll()
                        .requestMatchers(STUDENT_URLS).hasRole("STUDENT")
                        .requestMatchers(INSTRUCTOR_URLS).hasRole("INSTRUCTOR")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authBeanConfig.authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
