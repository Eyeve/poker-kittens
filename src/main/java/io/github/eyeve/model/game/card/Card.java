package io.github.eyeve.model.game.card;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Card {
    private Rank rank;
    private Suit suit;
}
