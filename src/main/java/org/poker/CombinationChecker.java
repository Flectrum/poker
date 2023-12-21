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
        }
        if(player.getCombinationCards().size() > 5){
            correctCombination();
        }
        if (player.getCombinationCards().size() < 5) {
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
        if (isCombinationNull() || !player.getCombination().equals(Combination.FULL_HOUSE)) {
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
                    if (!isCombinationNull() && (isCombinationEquals(Combination.PAIR) ||
                            isCombinationEquals(Combination.THREE_OF_KIND))) {
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
            }
            player.setCombinationCards(combination);
        }
    }

    public void correctCombination() {
                int minNumberThreeOfKind = Integer.MAX_VALUE;
                int minNumberPair = Integer.MAX_VALUE;
                int threeOfKind = 0;
                int pair = 0;
                Map<Integer, Long> cardsMap = player.getCombinationCards().stream()
                        .collect(Collectors.groupingBy(Card::getNumber, Collectors.counting()));
                for (Map.Entry<Integer, Long> card : cardsMap.entrySet()) {
                    if (card.getValue() == 3) {
                        if (minNumberThreeOfKind > card.getKey()) {
                            minNumberThreeOfKind = card.getKey();
                        }
                        threeOfKind++;
                    } else {
                        if (minNumberPair > card.getKey()) {
                            minNumberPair = card.getKey();
                        }
                        pair++;
                    }
                    if(threeOfKind == 2){
                        int finalMinNumberThreeOfKind = minNumberThreeOfKind;
                        player.getCombinationCards().remove(
                                player.getCombinationCards().stream()
                                        .filter(c -> c.getNumber() == finalMinNumberThreeOfKind)
                                        .findAny().orElseThrow());
                    } else if (isCombinationEquals(Combination.FULL_HOUSE) && pair == 2 ||
                    isCombinationEquals(Combination.TWO_PAIRS) && pair == 3){
                        int finalMinNumberPair = minNumberPair;
                        if(isCombinationEquals(Combination.FULL_HOUSE)) {
                            player.getCombinationCards().remove(
                                    player.getCombinationCards().stream()
                                            .filter(c -> c.getNumber() == finalMinNumberPair).findFirst().orElseThrow());
                        } else {
                            player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                    .filter(c -> c.getNumber() == finalMinNumberPair).toList());
                        }
                    }
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
            while (combination.size() != 5) {
                combination.remove(combination.get(0));
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
        List<Card> playerCombinationCardsCards = player.getCombinationCards();
        List<Card> currentCards = joinTableAndPlayerCards();
        currentCards.removeAll(player.getCombinationCards());
        int cardsToRemove = player.getCombinationCards().size();
        player.setCombinationCards(new ArrayList<>());
        while (player.getCombinationCards().size() != 5 - cardsToRemove) {
            if (!isCombinationNull() && (!isCombinationEquals(Combination.PAIR) || !isCombinationEquals(Combination.THREE_OF_KIND) ||
                    !isCombinationEquals(Combination.FOUR_OF_KIND))) {
                currentCards.sort(Comparator.comparing(Card::getNumber).reversed());
            }
            player.getCombinationCards().add(currentCards.get(0));
            currentCards.remove(0);
        }
        player.getCombinationCards().addAll(playerCombinationCardsCards);
    }

    private boolean isCombinationNull() {
        return player.getCombination() == null;
    }
}
