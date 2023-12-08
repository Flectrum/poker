package org.poker;

import java.util.HashSet;
import java.util.Set;

public class Deck {
    private final Set<Card> cards;
    public Deck() {
      cards = initialNewCardsDeck();
    }

    public Set<Card> getCards() {
        return cards;
    }

    private Set<Card> initialNewCardsDeck(){
        Set<Card> cardSet = new HashSet<>();
        Set<Integer> numbers = new HashSet<>();
        for(int i = 1; i < 14; i++){
            numbers.add(i);
        }
        Set<String> suits = new HashSet<>();
        suits.add("Spades");
        suits.add("Hearts");
        suits.add("Diamonds");
        suits.add("Clubs");
        for(String suit: suits){
            for(Integer number: numbers){
                cardSet.add(new Card(number,suit));
            }
        }
        return cardSet;
    }
}
