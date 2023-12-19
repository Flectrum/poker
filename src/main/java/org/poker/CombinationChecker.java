package org.poker;

import java.util.*;
import java.util.stream.Collectors;

public class CombinationChecker {

    private final Player player;
    private final List<Card> cardsOnTheTable;

    public CombinationChecker(Player player, List<Card> cardsOnTheTable) {
        this.player = player;
        this.cardsOnTheTable = cardsOnTheTable;
    }

    public void check() {
        checkForXOfKindCombination();
        if (isCombinationNull() || !isCombinationEquals(Combination.FOUR_OF_KIND) ||
                !isCombinationEquals(Combination.FULL_HOUSE)) {
            checkForFlush();
            if (isCombinationNull() || isCombinationEquals(Combination.FLUSH) ||
                    isCombinationEquals(Combination.TWO_PAIRS) ||
                    isCombinationEquals(Combination.THREE_OF_KIND) ||
                    isCombinationEquals(Combination.PAIR)) {
                checkForStraight(false);
            }
        }

        if (isCombinationNull()) {
            player.setCombination(Combination.HIGH_CARD);
            setHighCards(player);
        }
    }

    private boolean isCombinationEquals(Combination combination) {
        return player.getCombination().equals(combination);
    }

    private List<Card> joinTableAndPlayerCards() {
        List<Card> cards = new ArrayList<>(cardsOnTheTable);
        cards.addAll(Arrays.asList(player.getCards()));
        return cards;
    }

    public void checkForXOfKindCombination() {
        List<Card> currentCards = joinTableAndPlayerCards();
        Map<Integer, Integer> xOfKind = new HashMap<>();
        List<Card> combination = new ArrayList<>();
        for (Card card : currentCards) {
            if (xOfKind.containsKey(card.getNumber())) {
                xOfKind.put(card.getNumber(), xOfKind.get(card.getNumber()) + 1);
            } else {
                xOfKind.put(card.getNumber(), 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : xOfKind.entrySet()) {
            if (entry.getValue() == 4) {
                player.setCombination(Combination.FOUR_OF_KIND);
                combination = collectXOfKindCardsToList(currentCards, entry.getKey());
                player.setCombinationCards(combination);
                break;
            } else if (entry.getValue() == 3) {
                if (!isCombinationNull() && isCombinationEquals(Combination.PAIR)) {
                    player.setCombination(Combination.FULL_HOUSE);
                } else {
                    player.setCombination(Combination.THREE_OF_KIND);
                }
                combination.addAll(collectXOfKindCardsToList(currentCards, entry.getKey()));
            }
            if (entry.getValue() == 2) {
                if (!isCombinationNull()) {
                    if (isCombinationEquals(Combination.THREE_OF_KIND)) {
                        player.setCombination(Combination.FULL_HOUSE);
                    } else if (isCombinationEquals(Combination.PAIR)) {
                        player.setCombination(Combination.TWO_PAIRS);
                    }
                } else {
                    player.setCombination(Combination.PAIR);
                }
                combination.addAll(collectXOfKindCardsToList(currentCards, entry.getKey()));
            }
            player.setCombinationCards(combination);
        }
    }

    public List<Card> collectXOfKindCardsToList(List<Card> cards, int cardNumber) {
        return cards.stream().filter(a -> cardNumber == a.getNumber())
                .collect(Collectors.toList());
    }

    public void checkForStraight(boolean isAceEqualsZero) {
        List<Card> currentCards = joinTableAndPlayerCards();
        currentCards.sort(Comparator.comparing(Card::getNumber));
        List<Card> combination = new ArrayList<>();
        combination.add(currentCards.get(0));
        int cardSequence = 1;
        for (int i = 0; i < currentCards.size() - 1; i++) {
            if (currentCards.get(i).getNumber() + 1 == currentCards.get(i + 1).getNumber()) {
                cardSequence++;
                combination.add(currentCards.get(i + 1));
            } else if (cardSequence < 5 && currentCards.get(i).getNumber() != currentCards.get(i + 1).getNumber()) {
                cardSequence = 1;
                combination = new ArrayList<>();
            }
        }
        if (cardSequence >= 5) {
            if (!isCombinationNull() && isCombinationEquals(Combination.FLUSH)) {
                player.setCombination(Combination.STRAIGHT_FLUSH);
                if (combination.get(combination.size() - 1).getNumber() == 13) {
                    player.setCombination(Combination.ROYAL_FLUSH);
                }
            } else if (isCombinationNull() || !isCombinationEquals(Combination.STRAIGHT_FLUSH)
                    || !isCombinationEquals(Combination.ROYAL_FLUSH)) {
                player.setCombination(Combination.STRAIGHT);
            }
            player.setCombinationCards(combination);
            isAceEqualsZero = true;
        }
        if ((isCombinationNull() || (isCombinationEquals(Combination.FLUSH))) && !isAceEqualsZero) {
            currentCards.stream().filter(c -> c.getNumber() == 13).forEach(c -> c.setNumber(0));
            checkForStraight(true);
        }
    }

    public void checkForFlush() {
        List<Card> currentCards = joinTableAndPlayerCards();
        int cardSequence = 1;
        currentCards.sort(Comparator.comparing(Card::getSuit));
        List<Card> combination = new ArrayList<>();
        combination.add(currentCards.get(0));
        for (int i = 0; i < currentCards.size() - 1; i++) {
            if (currentCards.get(i).getSuit().equals(currentCards.get(i + 1).getSuit())) {
                cardSequence++;
                combination.add(currentCards.get(i + 1));
            } else {
                cardSequence = 1;
                combination = new ArrayList<>();
            }
            if (cardSequence >= 5) {
                player.setCombination(Combination.FLUSH);
                player.setCombinationCards(combination);
            }
        }
    }

    public void setHighCards(Player player) {
        List<Card> currentCards = joinTableAndPlayerCards();
        currentCards.removeAll(player.getCombinationCards());
        int cardsToRemove = player.getCombinationCards().size();
        player.setCombinationCards(new ArrayList<>());
        while (player.getCombinationCards().size() != 5 - cardsToRemove) {
            if (!isCombinationEquals(Combination.PAIR) || !isCombinationEquals(Combination.THREE_OF_KIND) ||
                    !isCombinationEquals(Combination.FOUR_OF_KIND)) {
                currentCards.sort(Comparator.comparing(Card::getNumber).reversed());
            }
            player.getCombinationCards().add(currentCards.get(0));
            currentCards.remove(0);
        }
    }

    private boolean isCombinationNull() {
        return player.getCombination() == null;
    }


}
