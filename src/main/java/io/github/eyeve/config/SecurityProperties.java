package io.github.eyeve.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app.security.jwt")
public record SecurityProperties(
        /*
         * Keep the signing secret outside git: environment variable, secret manager,
         * Docker/Kubernetes secret, CI secret, etc. The app only knows the property name.
         *
         * Base64 is used so the key is treated as bytes, not as a human password string.
         * Generate it with:
         *   openssl rand -base64 32
         * or PowerShell:
         *   [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
         */
        @NotBlank String secretBase64,

        /*
         * The issuer identifies who created the token. In real projects use a stable
         * value such as "https://auth.my-company.com" or the service name.
         */
        @NotBlank String issuer,

        /*
         * Access tokens should be short-lived. Long sessions are usually implemented
         * with refresh tokens stored/rotated server-side, which is intentionally kept
         * out of this minimal example.
         */
        @NotNull Duration accessTokenTtl
        // @Positive
) {
    public SecurityProperties {
        if (accessTokenTtl.isZero() || accessTokenTtl.isNegative()) {
            throw new IllegalArgumentException("accessTokenTtl must be greater than zero");
        }
    }
}
