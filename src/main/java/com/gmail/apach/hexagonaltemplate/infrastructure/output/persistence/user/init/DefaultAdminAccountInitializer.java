package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.init;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.admin.DefaultAdminConfigProperties;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository.RoleRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class DefaultAdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultAdminConfigProperties properties;

    private static final String DEFAULT_ADMIN_NAME = "default-admin";

    @Override
    public void run(String... args) {
        final var optionalAdmin = userRepository.findByUsername(properties.getUsername());
        if (optionalAdmin.isEmpty()) {
            final var defaultAdmin = UserEntity.builder()
                .username(properties.getUsername())
                .password(passwordEncoder.encode(properties.getPassword().trim()))
                .firstName(DEFAULT_ADMIN_NAME)
                .lastName(DEFAULT_ADMIN_NAME)
                .email(properties.getEmail())
                .enabled(true)
                .created(LocalDateTime.now())
                .createdBy(properties.getUsername())
                .isAdmin(true)
                .roles(new HashSet<>(roleRepository.findAll()))
                .build();
            userRepository.save(defaultAdmin);
        }
    }
}
