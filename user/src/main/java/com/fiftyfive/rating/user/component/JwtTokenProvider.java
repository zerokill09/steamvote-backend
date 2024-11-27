package com.fiftyfive.rating.user.component;

import com.fiftyfive.rating.user.domain.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60 * 24;

    private final SecretKey secretKey;

    public JwtTokenProvider (@Value("${jwt.password}") String key) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public String generateAccessToken(UserEntity user, Authentication authentication) {
        Map<String, Object> claims = createClaims(user, "access");
        return doGenerateAccessToken(claims, authentication.getName());
    }

    private String doGenerateAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt((new Date(System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY * 10000))
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(UserEntity user, String key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login_id", user.getUserId());
        claims.put("user_id", user.getId());
        claims.put("token_info", key);
        return claims;
    }

    public boolean validToken(String token) {
        try {
           Jws<Claims> parsed = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

           return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().get("user_id", String.class);
    }
}
