package io.github.eyeve.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import io.github.eyeve.config.SecurityProperties;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private static final int HS256_MIN_KEY_BYTES = 32;

    private final SecurityProperties securityProperties;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(securityProperties.secretBase64());
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("APP_JWT_SECRET_BASE64 must be valid Base64", exception);
        }

        /*
         * HS256 needs at least 256 bits. Short keys technically work in some libraries,
         * but they make brute-force attacks much easier, so fail during startup.
         */
        if (keyBytes.length < HS256_MIN_KEY_BYTES) {
            throw new IllegalStateException("APP_JWT_SECRET_BASE64 must decode to at least 32 bytes");
        }

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        /*
         * Resource Server integration validates signature, exp, nbf and token shape.
         * We explicitly pin HS256 so the app does not silently accept another algorithm.
         * We also validate issuer because a valid signature alone is not the whole
         * security boundary in systems where several services may issue JWTs.
         */
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(securityProperties.issuer()));
        return decoder;
    }
}
