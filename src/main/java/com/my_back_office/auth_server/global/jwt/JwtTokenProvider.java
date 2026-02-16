package com.my_back_office.auth_server.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.my_back_office.auth_server.domain.member.vo.Role;
import com.my_back_office.auth_server.domain.refreshtoken.RefreshToken;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-time}")
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateAccessToken(String email, Role role) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessTokenExpirationTime);

        return createToken(email, Role.ADMIN, TokenType.ACCESS_TOKEN, now, expiresAt);
    }

    public RefreshToken generateRefreshToken(String email, Long memberId) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + refreshTokenExpirationTime);

        String tokenValue = createToken(email, null, TokenType.REFRESH_TOKEN, now, expiresAt);
        return RefreshToken.createNew(tokenValue, memberId);
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        return decodedJWT.getSubject();
    }

    private String createToken(String email, Role role, TokenType type, Date now, Date expiresAt) {

        String roleClaimValue = (role != null) ? role.name() : null;

        return JWT.create()
            .withSubject(email)
            .withClaim("type", type.name())
            .withClaim("role", roleClaimValue)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .sign(algorithm);
    }
}

