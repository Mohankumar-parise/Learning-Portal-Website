package com.portal.authservice.services;


import com.portal.authservice.models.AuthUser;
import com.portal.authservice.repositories.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username);
        if (authUser == null) {
            throw new UsernameNotFoundException("User not Found");
        }
        return new User(authUser.getUsername(), authUser.getPassword(), Collections.singleton(new SimpleGrantedAuthority(authUser.getRole())));
    }
}
