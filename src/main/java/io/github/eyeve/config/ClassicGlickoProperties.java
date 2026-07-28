package io.github.eyeve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rating.glicko.classic")
public record ClassicGlickoProperties(
        double c,
        double defaultRating,
        double defaultRD
) { // TODO annotations like @NotNull as in SecurityProperties
}
