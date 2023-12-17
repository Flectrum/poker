package org.poker;

public class Card {
    private int number;
    private final String suit;

    public Card(int number, String suit) {
        this.number = number;
        this.suit = suit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "number=" + number +
                ", suit='" + suit + '\'' +
                '}';
    }
}
