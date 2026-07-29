package io.github.eyeve.model.rating;

import io.github.eyeve.model.Lobby;
import io.github.eyeve.model.game.Table;
import io.github.eyeve.model.game.TableResult;

public interface RatingEngine {
    RatingUpdate evaluate(TableResult tableResult);
}
