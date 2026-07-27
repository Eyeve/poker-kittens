package io.github.eyeve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

import io.github.eyeve.dto.AuthResponse;
import io.github.eyeve.config.SecurityProperties;
import io.github.eyeve.model.ApplicationUser;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;

    public AuthResponse issueAccessToken(ApplicationUser user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(securityProperties.accessTokenTtl());

        /*
         * Keep JWT claims small and non-sensitive. JWT payload is only Base64Url
         * encoded, not encrypted, so anyone with the token can read these fields.
         */
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(securityProperties.issuer())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("user_id", user.getId().toString())
                .claim("roles", List.of("ROLE_" + user.getRole().name()))
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new AuthResponse("Bearer", token, expiresAt);
    }
}
