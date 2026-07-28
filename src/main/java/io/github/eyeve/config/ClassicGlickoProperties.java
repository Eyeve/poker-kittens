package io.github.eyeve.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rating.glicko.classic")
public record ClassicGlickoProperties(
        @NotNull Double c,
        @NotNull Double defaultRating,
        @NotNull Double defaultRD
) { // TODO annotations like @NotNull as in SecurityProperties
}
