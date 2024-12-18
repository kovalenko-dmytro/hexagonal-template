package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.config.auth;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.ApplicationProfile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.filter.JwtAuthenticationFilter;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.config.api.OpenApiAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class RESTSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint appAuthenticationEntryPoint;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**", "/actuator/**")
            .csrf(AbstractHttpConfigurer::disable);

        if (activeProfile.contentEquals(ApplicationProfile.LOCAL)
            || activeProfile.contentEquals(ApplicationProfile.DEVELOP)) {
            http.authorizeHttpRequests(
                auth -> auth.requestMatchers(OpenApiAsset.getAssets()).permitAll());
        }

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/sign-in").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(smc ->
                smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(appAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
