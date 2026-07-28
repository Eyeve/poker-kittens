package io.github.eyeve.model.game;

import io.github.eyeve.model.game.card.Card;
import io.github.eyeve.model.game.card.Rank;
import io.github.eyeve.model.game.card.Suit;

import java.util.Stack;

public class Dealer {
    private Stack<Card> cards;

    public void getNewDeck() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {

    }
}
