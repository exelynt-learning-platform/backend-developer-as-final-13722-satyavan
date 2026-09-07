package com.resourcebookingsystem.config;

import com.resourcebookingsystem.model.Resource;
import com.resourcebookingsystem.model.Role;
import com.resourcebookingsystem.model.User;
import com.resourcebookingsystem.repository.ResourceRepository;
import com.resourcebookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder  implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            userRepository.save(User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.ROLE_USER)
                    .build());
        }

        if(resourceRepository.count() == 0){
            resourceRepository.save(Resource.builder()
                    .name("Conference Room Alpha")
                    .type("ROOM")
                    .description("Projector & Video Conference enabled")
                    .basePrice(new BigDecimal("150.00"))
                    .build());
            resourceRepository.save(Resource.builder()
                    .name("Company Van #1")
                    .type("VEHICLE")
                    .description("8-seater utility van")
                    .basePrice(new BigDecimal("75.50"))
                    .build());
        }

    }
}
