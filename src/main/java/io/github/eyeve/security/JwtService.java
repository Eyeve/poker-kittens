package io.github.eyeve.security;

import io.github.eyeve.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.apache.logging.log4j.Logger;

@Component
public class JwtService {

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-min.base}")
    private long jwtExpirationMin;

    @Value("${jwt.expiration-min.refresh}")
    private long refreshExpirationMin;

    public JwtAuthenticationDto generateAuthToken(String username) {
        return new JwtAuthenticationDto(generateJwtToken(username), generateRefreshToken(username));
    }

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken) {
        return new JwtAuthenticationDto(generateJwtToken(username), refreshToken);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException e) {
            LOGGER.error(e.getCause(), e);
            return false;
            // maybe add Exception handler
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateJwtToken(String username) {
        return generateToken(username, jwtExpirationMin);
    }

    private String generateRefreshToken(String username) {
        return generateToken(username, refreshExpirationMin);
    }

    private String generateToken(String username, long expirationMin) {
        Date expiration =  Date.from(LocalDateTime.now()
                .plusMinutes(expirationMin)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .subject(username)
                .expiration(expiration)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
