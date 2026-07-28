package io.github.eyeve.model.rating;

import io.github.eyeve.config.ClassicGlickoProperties;
import io.github.eyeve.model.Lobby;
import io.github.eyeve.model.User;
import io.github.eyeve.model.game.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClassicGlickoRatingEngine implements RatingEngine {
    private final ClassicGlickoProperties properties;

    @Override
    public RatingUpdate evaluate(Lobby lobby) {
        return null;
    }

    private Map<Long, Double> getCurrentRatings(List<User> users) {
        Map<Long, Double> map = new HashMap<>();
        for (User user : users) {
            if (user.getRating() != null) {
                map.put(user.getId(), user.getRating());
            } else {
                map.put(user.getId(), properties.defaultRating());
            }
        }
        return map;
    }

    private Map<Long, Double> getCurrentRD(List<User> users) {
        Map<Long, Double> map = new HashMap<>();
        for (User user : users) {
            if (user.getRd() != null && user.getLastRatingUpdate() != null) {
                map.put(
                        user.getId(),
                        Math.min(
                                Math.sqrt(
                                        user.getRd() * user.getRd() +
                                                properties.c() * properties.c() *
                                                        Duration.between(user.getLastRatingUpdate(),
                                                                LocalDateTime.now()
                                                        ).toDays()
                                ),
                                properties.defaultRD()
                        )
                );
            } else {
                map.put(user.getId(), properties.defaultRD());
            }
        }
        return map;
    }
    // TODO formulas
}
