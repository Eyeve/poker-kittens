package io.github.eyeve.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable
public class Rating {
    private Double score;
    private Double rd;
    private Double volatility;
    private LocalDateTime lastRatingUpdate;
}
