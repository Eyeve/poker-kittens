package io.github.eyeve.model.rating;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class RatingUpdate {
    private final Map<Long, Double> ratingUpdate;
    private final Map<Long, Double> rdUpdate;
    private final Map<Long, Double> volatilityUpdate;
}
