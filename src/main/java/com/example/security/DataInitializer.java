package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        var admin = UserEntity.builder()
                .username("admin")
                .email("admin@vaultvintage.com")
                .displayName("Admin")
                .password(passwordEncoder.encode("admin123"))
                .role("ADMIN")
                .build();
        userRepository.save(admin);
        System.out.println("Default admin created: admin / admin123");
    }
}
