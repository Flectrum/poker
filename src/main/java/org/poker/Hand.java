package org.poker;

import java.util.*;

public class Hand {
    private Player winner;
    private final List<Player> players;
    private final Deck deck;
    private List<Card> cardsOnTheTable;

    public Hand(List<Player> players) {
        this.players = players;
        deck = new Deck();
        cardsOnTheTable = new ArrayList<>();
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Card> getCardsOnTheTable() {
        return cardsOnTheTable;
    }

    public void setCardsOnTheTable(List<Card> cardsOnTheTable) {
        this.cardsOnTheTable = cardsOnTheTable;
    }

    public Player getWinner() {
        return winner;
    }

    public void init() {
        startGame();
        for (Player player : players) {
            checkForFlush(player);
            if (player.getCombination() == null || !player.getCombination().equals("Straight Flush")) {
                checkForXOfKindCombination(player);
                if (player.getCombination() == null || !player.getCombination().equals("Four of Kind") ||
                        !player.getCombination().equals("Full House")) {
                    checkForFlush(player);
                    if (player.getCombination() == null || !player.getCombination().equals("Flush")) {
                        checkForStraight(player, false);
                    }
                }
            }
            if (player.getCombination() == null) {
                findHighCard(player, joinTableAndPlayerCards(player));
            }
        }
        comparePlayersCombinations();
    }

    public void startGame() {
        dealCardsToTheAllPlayers();
        for (int i = 0; i < 5; i++) {
            cardsOnTheTable.add(takeCardFromTheDeck());
        }
//        cardsOnTheTable.add(new Card(12, "Diamonds"));
//        cardsOnTheTable.add(new Card(10, "Diamonds"));
//        cardsOnTheTable.add(new Card(11, "Diamonds"));
//        cardsOnTheTable.add(new Card(9, "Diamonds"));
    }

    public void dealCardsToTheAllPlayers() {
        for (Player player : players) {
            dealCardsToThePlayer(player);
        }
    }

    public void dealCardsToThePlayer(Player player) {
        Card[] cards = new Card[2];
        for (int i = 0; i < 2; i++) {
            Card card = takeCardFromTheDeck();
            cards[i] = card;
        }
//        cards[0] = new Card(13, "Diamonds");
//        cards[1] = new Card(7, "Diamonds");
        player.setCards(cards);
    }

    private Card takeCardFromTheDeck() {
        Card card = new CardRandomizer().getRandomElement(deck.getCards());
        removeCardFromDeck(card);
        return card;
    }

    private void removeCardFromDeck(Card card) {
        deck.getCards().remove(card);
    }

    private List<Card> joinTableAndPlayerCards(Player player) {
        List<Card> cards = new ArrayList<>(cardsOnTheTable);
        cards.addAll(Arrays.asList(player.getCards()));
        return cards;
    }

    public void checkForXOfKindCombination(Player player) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
        Map<Integer, Integer> xOfKind = new HashMap<>();
        for (Card card : currentCards) {
            if (xOfKind.containsKey(card.getNumber())) {
                xOfKind.put(card.getNumber(), xOfKind.get(card.getNumber()) + 1);
            } else {
                xOfKind.put(card.getNumber(), 1);
            }
        }
        int cardNumberToRemove = -1;
        for (Map.Entry<Integer, Integer> entry : xOfKind.entrySet()) {
            if (entry.getValue() == 4) {
                player.setCombination("Four of Kind");
            } else if (entry.getValue() == 3) {
                player.setCombination("Three of Kind");
                cardNumberToRemove = entry.getKey();
            } else if (entry.getValue() == 2) {
                player.setCombination("Pair");
                cardNumberToRemove = entry.getKey();
            }
        }
        if (cardNumberToRemove != -1) {
            xOfKind.remove(cardNumberToRemove);
            checkForFullHouseAndTwoPairs(player, xOfKind);
        }
    }

    public void checkForFullHouseAndTwoPairs(Player player, Map<Integer, Integer> xOfKind) {
        for (Map.Entry<Integer, Integer> entry : xOfKind.entrySet()) {
            if (player.getCombination().equals("Three of Kind") && entry.getValue() >= 2) {
                player.setCombination("Full House");
            }
            if (player.getCombination().equals("Pair")) {
                if (entry.getValue() == 3) {
                    player.setCombination("Full House");
                } else if (entry.getValue() == 2) {
                    player.setCombination("Two Pairs");
                }
            }
        }
    }

    public void checkForStraight(Player player, boolean isAceEqualsZero) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
        currentCards.sort(Comparator.comparing(Card::getNumber));
        currentCards.sort(Comparator.comparing(Card::getSuit));
        List<Card> combination = new ArrayList<>();
        int cardSequence = 1;
        for (int i = 0; i < currentCards.size() - 1; i++) {
            if (currentCards.get(i).getNumber() + 1 == currentCards.get(i + 1).getNumber()) {
                cardSequence++;
                combination.add(currentCards.get(i + 1));
            } else {
                cardSequence = 1;
                combination = new ArrayList<>();
            }
            if (cardSequence >= 5) {
                if (player.getCombination() != null && player.getCombination().equals("Flush")) {
                    player.setCombination("Straight Flush");
                    if (combination.get(combination.size() - 1).getNumber() == 13) {
                        player.setCombination("Royal Flush");
                    }
                } else if (player.getCombination() == null || (!player.getCombination().equals("Straight Flush")
                        && !player.getCombination().equals("Royal Flush"))) {
                    player.setCombination("Straight");
                }
                player.setHighCard(combination.get(combination.size() - 1));
                isAceEqualsZero = true;
            }
        }
        if ((player.getCombination() == null || (player.getCombination().equals("Flush"))) && !isAceEqualsZero) {
            currentCards.stream().filter(c -> c.getNumber() == 13).forEach(c -> c.setNumber(0));
            checkForStraight(player, true);
        }
    }

    public void checkForFlush(Player player) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
        int cardSequence = 1;
        currentCards.sort(Comparator.comparing(Card::getSuit));
        for (int i = 0; i < currentCards.size() - 1; i++) {
            if (currentCards.get(i).getSuit().equals(currentCards.get(i + 1).getSuit())) {
                cardSequence++;

            } else {
                cardSequence = 1;
            }
            if (cardSequence >= 5) {
                player.setCombination("Flush");
                String finalSuit = currentCards.get(i).getSuit();
                currentCards.stream()
                        .filter(a -> a.getSuit().equals(finalSuit))
                        .max(Comparator.comparing(Card::getNumber))
                        .ifPresent(player::setHighCard);
            }
        }
    }

    public void findHighCard(Player player, List<Card> cards) {
        cards.sort(Comparator.comparing(Card::getNumber));
        if (player.getCombination() == null) {
            player.setCombination("High Card");
        } else {
            player.setHighCard(cards.get(cards.size() - 1));
        }
    }

    public void comparePlayersCombinations() {
        int winnerRate = 0;
        for (Player player : players) {
            switch (player.getCombination()) {
                case "High Card" -> player.setCombinationRate(1);
                case "Pair" -> player.setCombinationRate(2);
                case "Two Pairs" -> player.setCombinationRate(3);
                case "Three of Kind" -> player.setCombinationRate(4);
                case "Straight" -> player.setCombinationRate(5);
                case "Flush" -> player.setCombinationRate(6);
                case "Full House" -> player.setCombinationRate(7);
                case "Four of Kind" -> player.setCombinationRate(8);
                case "Straight Flush" -> player.setCombinationRate(9);
                case "Royal Flush" -> player.setCombinationRate(10);
            }
            if (winnerRate < player.getCombinationRate()) {
                winnerRate = player.getCombinationRate();
                winner = player;
            }
        }
    }
}

