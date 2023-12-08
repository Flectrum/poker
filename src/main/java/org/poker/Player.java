package org.poker;

public class Player {
    private String name;
    private Card [] cards;
    private String combination;
    private boolean isPlaying;
    private Card highCard;
    private int combinationRate;
    public Player() {
        cards = new Card[2];
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

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getCombinationRate() {
        return combinationRate;
    }

    public void setCombinationRate(int combinationRate) {
        this.combinationRate = combinationRate;
    }

    public Card getHighCard() {
        return highCard;
    }

    public void setHighCard(Card highCard) {
        this.highCard = highCard;
    }
}
