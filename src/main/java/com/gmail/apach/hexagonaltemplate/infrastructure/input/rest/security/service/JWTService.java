package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.service;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {

    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateAccessToken(User user);
    String generateAccessToken(Map<String, Object> Claims, User user);
    long getAccessTokenExpirationTime();
    boolean isTokenValid(String token, UserDetails userDetails);
}
