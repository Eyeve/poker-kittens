package io.github.eyeve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.github.eyeve.config.SecurityProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PokerKittensApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PokerKittensApplication.class, args);
    }
}
