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

    private final String[] openUrls = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private final String[] adminUrls = {
            // http://localhost:7070/api/v1/admin/panel
            "/api/v1/admin/panel"
    };

    private final String[] userUrls = {
            "/api/v1/welcome/message"
    };

    private final AuthBeanConfig authBeanConfig;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf->csrf.disable());
        httpSecurity.authorizeHttpRequests(auth-> { auth
                .requestMatchers(openUrls).permitAll()
                .requestMatchers(userUrls).hasRole("STUDENT")
                .requestMatchers(adminUrls).hasRole("INSTRUCTOR")
                .anyRequest().authenticated();
        }).authenticationProvider(authBeanConfig.authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
