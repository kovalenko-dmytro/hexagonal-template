package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class GraphQlSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain graphQlSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/graphiql**", "/graphql")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/graphiql**").permitAll()
                    .requestMatchers("/graphql").permitAll())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
