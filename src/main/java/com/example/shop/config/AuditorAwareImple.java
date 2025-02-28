package com.example.shop.config;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImple implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();


        String email = "";

        if(authentication != null) {
            email = authentication.getName();
        }

        return Optional.of(email);
    }
}
