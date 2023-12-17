package org.poker;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private final List<Player> winnerList;
    private final List<Player> players;
    private final Deck deck;
    private List<Card> cardsOnTheTable;
    private boolean isSplit;

    public Hand(List<Player> players) {
        this.players = players;
        deck = new Deck();
        cardsOnTheTable = new ArrayList<>();
        winnerList = new ArrayList<>();
        isSplit = false;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setCardsOnTheTable(List<Card> cardsOnTheTable) {
        this.cardsOnTheTable = cardsOnTheTable;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public List<Player> getWinnerList() {
        return winnerList;
    }

    public void init() {
        startGame();
        for (Player player : players) {
            checkForFlush(player);
            if (player.getCombination() == null || !player.getCombination().equals(Combination.STRAIGHT_FLUSH)) {
                checkForXOfKindCombination(player);
                if (player.getCombination() == null || !player.getCombination().equals(Combination.FOUR_OF_KIND) ||
                        !player.getCombination().equals(Combination.FULL_HOUSE)) {
                    checkForFlush(player);
                    if (player.getCombination() == null || !player.getCombination().equals(Combination.FLUSH)) {
                        checkForStraight(player, false);
                    }
                }
            }
            if (player.getCombination() == null) {
                player.setCombination(Combination.HIGH_CARD);
                setHighCards(player);
            }
        }

    }

    public void startGame() {
        dealCardsToTheAllPlayers();
        for (int i = 0; i < 5; i++) {
            cardsOnTheTable.add(takeCardFromTheDeck());
        }
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
                if (player.getCombination() != null && player.getCombination().equals(Combination.PAIR)) {
                    player.setCombination(Combination.FULL_HOUSE);
                } else {
                    player.setCombination(Combination.THREE_OF_KIND);
                }
                combination.addAll(collectXOfKindCardsToList(currentCards, entry.getKey()));
            }
            if (entry.getValue() == 2) {
                if (player.getCombination() != null) {
                    if (player.getCombination().equals(Combination.THREE_OF_KIND)) {
                        player.setCombination(Combination.FULL_HOUSE);
                    } else if (player.getCombination().equals(Combination.PAIR)) {
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


    public void checkForStraight(Player player, boolean isAceEqualsZero) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
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
            if (player.getCombination() != null && player.getCombination().equals(Combination.FLUSH)) {
                player.setCombination(Combination.STRAIGHT_FLUSH);
                if (combination.get(combination.size() - 1).getNumber() == 13) {
                    player.setCombination(Combination.ROYAL_FLUSH);
                }
            } else if (player.getCombination() == null || (!player.getCombination().equals(Combination.STRAIGHT_FLUSH)
                    && !player.getCombination().equals(Combination.ROYAL_FLUSH))) {
                player.setCombination(Combination.STRAIGHT);
            }
            player.setCombinationCards(combination);
            isAceEqualsZero = true;
        }
        if ((player.getCombination() == null || (player.getCombination().equals(Combination.FLUSH))) && !isAceEqualsZero) {
            currentCards.stream().filter(c -> c.getNumber() == 13).forEach(c -> c.setNumber(0));
            checkForStraight(player, true);
        }
    }

    public void checkForFlush(Player player) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
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
        List<Card> currentCards = joinTableAndPlayerCards(player);
        currentCards.removeAll(player.getCombinationCards());
        int cardsToRemove = player.getCombinationCards().size();
        player.setCombinationCards(new ArrayList<>());
        while (player.getCombinationCards().size() != 5 - cardsToRemove) {
            if (player.getCombination() != Combination.PAIR || player.getCombination() != Combination.THREE_OF_KIND ||
                    player.getCombination() != Combination.FOUR_OF_KIND) {
                currentCards.sort(Comparator.comparing(Card::getNumber).reversed());
            }
            player.getCombinationCards().add(currentCards.get(0));
            currentCards.remove(0);
        }
    }

    public void getWinnerByHighCard(List<Player> players) {
        if (players.size() == 1) {
            winnerList.add(players.get(0));
            return;
        }
        List<Player> playersToRemove = new ArrayList<>();
        int highCard = players.get(0).getCombinationCards().get(0).getNumber();
        for (Player currentPlayer : players) {
            if (currentPlayer.getCombinationCards().isEmpty()) {
                isSplit = true;
                return;
            }
            if (highCard < currentPlayer.getCombinationCards().get(0).getNumber()) {
                playersToRemove.add(players.get(0));
            } else if (highCard <= currentPlayer.getCombinationCards().get(0).getNumber()) {
                highCard = currentPlayer.getCombinationCards().get(0).getNumber();
                currentPlayer.getCombinationCards().remove(0);
            } else {
                playersToRemove.add(currentPlayer);
            }
        }
        players.removeAll(playersToRemove);
        getWinnerByHighCard(players);
    }

    public void getWinnerByXOfKind(List<Player> players) {
        List<Player> winners = new ArrayList<>();
        int cardNumber = players.get(0).getCombinationCards().get(0).getNumber();
        for (Player player : players) {
            int playersCardNumber = player.getCombinationCards().get(0).getNumber();
            if (playersCardNumber > cardNumber) {
                cardNumber = playersCardNumber;
                winners = new ArrayList<>();
                winners.add(player);
            } else if (playersCardNumber == cardNumber) {
                winners.add(player);
                setHighCards(player);
            }
        }
        getWinnerByHighCard(winners);
    }
}


// Full House - x = combination.stream().filter(count == 3 ), if x == x, pair, else - split

// Two Pair - Comparator.compare(combination.getNumber()), combination.get(size -1). else combination.get(0), else - highCard
