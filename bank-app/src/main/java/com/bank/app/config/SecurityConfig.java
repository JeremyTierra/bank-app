package com.bank.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for security-related beans.
 * Provides BCrypt password encoder for secure password storage.
 */
@Configuration
public class SecurityConfig {
    
    /**
     * Provides a BCrypt password encoder bean for password encryption.
     * BCrypt is a strong adaptive hash function designed for password hashing.
     * 
     * @return PasswordEncoder instance using BCrypt algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
