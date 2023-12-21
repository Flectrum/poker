package org.poker;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private List<Player> winnerList;
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
            CombinationChecker combinationChecker = new CombinationChecker(player, cardsOnTheTable);
            combinationChecker.check();
        }
        getWinnerByXOfKind(players);
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
        Set<Player> tempWinnerList = new HashSet<>();
        Combination highestCombination = null;
        int highestCardNumber = -1;
        int secondHighestCardNumber = -1;
        for (Player player : players) {
            Map<Integer, Long> cardsMap = player.getCombinationCards().stream()
                    .collect(Collectors.groupingBy(Card::getNumber, Collectors.counting()));
            for (Map.Entry<Integer, Long> entry : cardsMap.entrySet()) {
                if (entry.getValue() == 4) {
                    if (entry.getKey() > highestCardNumber) {
                        highestCombination = player.getCombination();
                        tempWinnerList = new HashSet<>();
                        tempWinnerList.add(player);
                    } else if (entry.getKey() == highestCardNumber) {
                        tempWinnerList.add(player);
                    }
                } else if (entry.getValue() == 3) {
                    if (player.getCombination().equals(Combination.FULL_HOUSE)) {
                        if (highestCombination == null || !highestCombination.equals(Combination.FOUR_OF_KIND)) {
                            if (entry.getKey() > highestCardNumber) {
                                highestCardNumber = entry.getKey();
                                highestCombination = player.getCombination();
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                            } else if (entry.getKey() == highestCardNumber) {
                                tempWinnerList.add(player);
                                player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                        .filter(c -> c.getNumber() != entry.getKey()).toList());

                            }
                        }
                    } else if ((highestCombination == null || !highestCombination.equals(Combination.FOUR_OF_KIND))
                            && player.getCombination().equals(Combination.THREE_OF_KIND)) {
                        if (entry.getKey() > highestCardNumber) {
                            tempWinnerList = new HashSet<>();
                            tempWinnerList.add(player);
                        } else if (entry.getKey() == highestCardNumber) {
                            tempWinnerList.add(player);
                        }
                        highestCombination = player.getCombination();
                    }
                } else if (entry.getValue() == 2) {
                    if (player.getCombination().equals(Combination.FULL_HOUSE)) {
                        if (highestCombination == null || !highestCombination.equals(Combination.FOUR_OF_KIND)) {
                            if (entry.getKey() > secondHighestCardNumber) {
                                secondHighestCardNumber = entry.getKey();
                                highestCombination = player.getCombination();
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                            } else if (entry.getKey() == highestCardNumber) {
                                tempWinnerList.add(player);
                                player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                        .filter(c -> c.getNumber() != entry.getKey()).toList());
                            }
                        }
                    } else if ( highestCombination == null || !highestCombination.equals(Combination.THREE_OF_KIND)) {
                        if (player.getCombination().equals(Combination.TWO_PAIRS)) {
                            if (entry.getKey() > Math.max(highestCardNumber, secondHighestCardNumber)) {
                                highestCardNumber = entry.getKey();
                                highestCombination = player.getCombination();
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                            } else if (entry.getKey() == highestCardNumber) {
                                tempWinnerList.add(player);
                                player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                        .filter(c -> c.getNumber() != entry.getKey()).toList());
                            }
                        } else if (player.getCombination().equals(Combination.PAIR)) {
                            if (entry.getKey() > highestCardNumber) {
                                highestCombination = player.getCombination();
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                            } else if (entry.getKey() == highestCardNumber) {
                                tempWinnerList.add(player);
                            }
                        }
                    }
                }
            }
        }
        winnerList = tempWinnerList.stream().toList();
    }
}