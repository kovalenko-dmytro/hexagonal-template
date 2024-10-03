package com.gmail.apach.hexagonaltemplate.infrastructure.common.auth;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.jwt.JWTConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final JWTConfigProperties jwtConfigProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user);
    }

    public long getAccessTokenExpirationTime() {
        return jwtConfigProperties.getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private String buildToken(Map<String, Object> extraClaims, User user) {
        final var now = System.currentTimeMillis();
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + jwtConfigProperties.getExpiration()))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
