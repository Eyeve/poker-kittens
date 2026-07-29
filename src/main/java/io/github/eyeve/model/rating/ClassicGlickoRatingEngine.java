package io.github.eyeve.model.rating;

import io.github.eyeve.config.ClassicGlickoProperties;
import io.github.eyeve.model.User;
import io.github.eyeve.model.game.TableResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClassicGlickoRatingEngine implements RatingEngine {
    private final ClassicGlickoProperties properties;
    private final static double q = Math.log(10) / 400;


    @Override
    public RatingUpdate evaluate(TableResult tableResult) {
        Map<Long, Double> ratingUpdate = new HashMap<>();
        Map<Long, Double> rdUpdate = new HashMap<>();

        Map<Long, Double> currentRd = getCurrentRD(tableResult.sortedUsers());

        for (User user : tableResult.sortedUsers()) {
            List<User> enemies = new ArrayList<>(tableResult.sortedUsers());
            enemies.remove(user);

            double rating = user.getRating() == null ? properties.defaultRating() : user.getRating().getScore();
            double rd = currentRd.get(user.getId());

            List<Double> g = g(enemies, currentRd);
            List<Double> E = E(user, enemies, g);
            double dSquare = dSquare(g, E);
            List<Double> s = new ArrayList<>();

            boolean isWon = true;
            for (User u : tableResult.sortedUsers()) {
                if (u.equals(user)) {
                    isWon = false;
                    continue;
                }
                s.add((double) (isWon ? 1 : 0));
            }


            ratingUpdate.put(user.getId(), rating + ratingUpd(rd, dSquare, g, E, s));
            rdUpdate.put(user.getId(), rdUpd(rd, dSquare));
        }
        return new RatingUpdate(ratingUpdate, rdUpdate, null);
    }

    private double rdUpd(double rd, double dSquare) {
        double k = 1 / (rd * rd) + 1 / dSquare;
        return Math.sqrt(1 / k);
    }

    private double ratingUpd(double rd, double dSquare, List<Double> g, List<Double> E, List<Double> s) {
        double sum = 0;
        for (int i = 0; i < g.size(); i++) {
            sum += g.get(i) * (s.get(i) - E.get(i));
        }
        double k = q / (1 / (rd * rd) + 1 / dSquare);
        return k * sum;
    }

    private Map<Long, Double> getCurrentRD(List<User> users) {
        Map<Long, Double> map = new HashMap<>();
        for (User user : users) {
            if (user.getRating().getRd() != null && user.getRating().getLastRatingUpdate() != null) {
                Long id = user.getId();
                double rd = user.getRating().getRd();
                double duration = Duration.between(user.getRating().getLastRatingUpdate(), LocalDateTime.now()).toDays(); // TODO
                double newRd = Math.sqrt(rd * rd + properties.c() * properties.c() * duration);
                map.put(id, Math.min(newRd, properties.defaultRD()));
            } else {
                map.put(user.getId(), properties.defaultRD());
            }
        }
        return map;
    }

    private List<Double> g(List<User> enemies, Map<Long, Double> currentRd) {
        List<Double> result = new ArrayList<>();
        for (User user : enemies) {
            double rd = currentRd.get(user.getId());
            double g = 1 / (Math.sqrt(1 + 3 * q * q * rd * rd / (Math.PI * Math.PI)));
            result.add(g);
        }
        return result;
    }

    private List<Double> E(User user, List<User> enemies, List<Double> g) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < enemies.size(); i++) {
            double e = 1 / (1 + Math.pow(10, -g.get(i) * (user.getRating().getScore() - enemies.get(i).getRating().getScore()) / 400));
            result.add(e);
        }
        return result;
    }

    private double dSquare(List<Double> g, List<Double> E) {
        double sum = 0;
        for (int i = 0; i < g.size(); i++) {
            sum += g.get(i) * g.get(i) * E.get(i) * (1 - E.get(i));
        }
        return 1 / (q * q * sum);
    }
}
