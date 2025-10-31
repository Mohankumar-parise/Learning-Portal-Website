package com.portal.authservice.repositories;

import com.portal.authservice.models.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthUser findByUsername(String username);
    AuthUser findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
