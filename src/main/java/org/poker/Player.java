package org.poker;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Card[] cards;
    private Combination combination1;
    private boolean isPlaying;
    private List<Card> combinationCards;

    public Player() {
        cards = new Card[2];
        combinationCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public List<Card> getCombinationCards() {
        return combinationCards;
    }

    public void setCombinationCards(List<Card> combinationCards) {
        this.combinationCards = combinationCards;
    }

    public Combination getCombination1() {
        return combination1;
    }

    public void setCombination1(Combination combination1) {
        this.combination1 = combination1;
    }
}
