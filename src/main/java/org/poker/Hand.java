package org.poker;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private List <Player> winnerList;
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

    public void init() {
        startGame();
        for (Player player : players) {
            checkForFlush(player);
            if (player.getCombination1() == null || !player.getCombination1().equals(Combination.STRAIGHT_FLUSH)) {
                checkForXOfKindCombination(player);
                if (player.getCombination1() == null || !player.getCombination1().equals(Combination.FOUR_OF_KIND) ||
                        !player.getCombination1().equals(Combination.FULL_HOUSE)) {
                    checkForFlush(player);
                    if (player.getCombination1() == null  || !player.getCombination1().equals(Combination.FLUSH)) {
                        checkForStraight(player, false);
                    }
                }
            }
            if (player.getCombination1() == null) {
                player.setCombination1(Combination.HIGH_CARD);
                setHighCards(player, 5 - player.getCombinationCards().size());
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
                player.setCombination1(Combination.FOUR_OF_KIND);
                combination = collectXOfKindCardsToList(currentCards, entry.getKey());
                player.setCombinationCards(combination);
                break;
            } else if (entry.getValue() == 3) {
                if(player.getCombination1() != null && player.getCombination1().equals(Combination.PAIR)){
                    player.setCombination1(Combination.FULL_HOUSE);
                } else {
                    player.setCombination1(Combination.THREE_OF_KIND);
                }
                combination.addAll(collectXOfKindCardsToList(currentCards, entry.getKey()));
            }
            if (entry.getValue() == 2) {
                if(player.getCombination1() != null) {
                    if (player.getCombination1().equals(Combination.THREE_OF_KIND)) {
                        player.setCombination1(Combination.FULL_HOUSE);
                    } else if (player.getCombination1().equals(Combination.PAIR)) {
                        player.setCombination1(Combination.TWO_PAIRS);
                    }
                }else {
                    player.setCombination1(Combination.PAIR);
                }
                combination.addAll(collectXOfKindCardsToList(currentCards, entry.getKey()));
            }
            player.setCombinationCards(combination);
        }
    }

    public List<Card> collectXOfKindCardsToList(List<Card> cards, int cardNumber){
        return  cards.stream().filter(a -> cardNumber == a.getNumber())
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
            if (player.getCombination1() != null && player.getCombination1().equals(Combination.FLUSH)) {
                player.setCombination1(Combination.STRAIGHT_FLUSH);
                if (combination.get(combination.size() - 1).getNumber() == 13) {
                    player.setCombination1(Combination.ROYAL_FLUSH);
                }
            } else if (player.getCombination1() == null || (!player.getCombination1().equals(Combination.STRAIGHT_FLUSH)
                    && !player.getCombination1().equals(Combination.ROYAL_FLUSH))) {
                player.setCombination1(Combination.STRAIGHT);
            }
            player.setCombinationCards(combination);
            isAceEqualsZero = true;
        }
        if ((player.getCombination1() == null || (player.getCombination1().equals(Combination.FLUSH))) && !isAceEqualsZero) {
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
                player.setCombination1(Combination.FLUSH);
                player.setCombinationCards(combination);
            }
        }
    }

    public void setHighCards(Player player, int numberOfCards) {
        List<Card> currentCards = joinTableAndPlayerCards(player);
        while (player.getCombinationCards().size() != numberOfCards) {
            currentCards.sort(Comparator.comparing(Card::getNumber).reversed());
            player.getCombinationCards().add(currentCards.get(0));
            currentCards.remove(0);
        }
    }

    public void getWinnerByHighCard(List<Player> players) {
        List<Player> playersWithSameHighCard = new ArrayList<>();
        List<Player> playersToRemove = new ArrayList<>();
        int highCard = -1;
        for (Player currentPlayer : players) {
            if (currentPlayer.getCombinationCards().isEmpty()){
                if (players.size() == 1) {
                    winnerList.add(currentPlayer);
                } else {
                    isSplit = true;
                }
                return;
            }
                if (highCard <= currentPlayer.getCombinationCards().get(0).getNumber()) {
                    highCard = currentPlayer.getCombinationCards().get(0).getNumber();
                    currentPlayer.getCombinationCards().remove(0);
                    playersWithSameHighCard.add(currentPlayer);
                } else {
                    playersToRemove.add(currentPlayer);
                }
        }
        players.removeAll(playersToRemove);
        getWinnerByHighCard(playersWithSameHighCard);
    }
}

// Royal Flush - Win, =< 2 players - split
// Straight Flush, Straight - combination.size() -1 , else - split

// Flush - combination.size() -1, if x == x, repeat, if combination.isEmpty - split

// Four of Kind - combination.get(0), if x == x, joinTableAndPlayerCards(player).remove(combination), highCard,
// else - split

// Full House - x = combination.stream().filter(count == 3 ), if x == x, pair, else - split

// Three of Kind - combination.get(0), if x == x, while(combination != 5) combination.add(highCard), else - split

// Two Pair - Comparator.compare(combination.getNumber()), combination.get(size -1). else combination.get(0), else - highCard

// pair - combination.get(0), else highCard <= 3