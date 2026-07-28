package io.github.eyeve.model.rating;

import io.github.eyeve.model.Lobby;
import io.github.eyeve.model.game.Table;

public interface RatingEngine {
    RatingUpdate evaluate(Lobby lobby); // TODO ? make new class TableResult
}
