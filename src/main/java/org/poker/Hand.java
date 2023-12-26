package org.poker;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private Set<Player> winnerList;
    private final Set<Player> players;
    private final Deck deck;
    private List<Card> cardsOnTheTable;

    private Combination highestCombination;
    private boolean isSplit;

    private int highestCombinationRate;
    private boolean hasSameCards;

    public Hand(Set<Player> players) {
        this.players = players;
        deck = new Deck();
        cardsOnTheTable = new ArrayList<>();
        winnerList = new HashSet<>();
        isSplit = false;
        highestCombination = Combination.HIGH_CARD;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setCardsOnTheTable(List<Card> cardsOnTheTable) {
        this.cardsOnTheTable = cardsOnTheTable;
    }

    public List<Card> getCardsOnTheTable() {
        return cardsOnTheTable;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public Set<Player> getWinnerList() {
        return winnerList;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void init() {
        for (Player player : players) {
            CombinationChecker combinationChecker = new CombinationChecker(player, cardsOnTheTable);
            combinationChecker.check();
            if (cardsOnTheTable.size() == 5) {
                int tempCombinationRate = findHighestCombination(player);
                if (tempCombinationRate == highestCombinationRate) {
                    winnerList.add(player);

                } else if (tempCombinationRate > highestCombinationRate) {
                    highestCombinationRate = tempCombinationRate;
                    highestCombination = player.getCombination();
                    winnerList = new HashSet<>();
                    winnerList.add(player);
                }
            }
        }
        if (highestCombinationRate != 1 && highestCombinationRate < 5
                || highestCombinationRate == 7 || highestCombinationRate == 8) {
            getWinnerByXOfKind(winnerList);
        }
        if (highestCombinationRate == 1 || highestCombinationRate == 5 || highestCombinationRate == 6
                || highestCombinationRate == 9 || highestCombinationRate == 10 || hasSameCards) {
            getWinnerByHighCard(winnerList);
        }
        if (winnerList.size() > 1) {
            isSplit = true;
        }
    }


    private int findHighestCombination(Player player) {
        int combinationRate = 1;
        switch (player.getCombination()) {
            case ROYAL_FLUSH -> combinationRate = 10;
            case STRAIGHT_FLUSH -> combinationRate = 9;
            case FOUR_OF_KIND -> combinationRate = 8;
            case FULL_HOUSE -> combinationRate = 7;
            case FLUSH -> combinationRate = 6;
            case STRAIGHT -> combinationRate = 5;
            case THREE_OF_KIND -> combinationRate = 4;
            case TWO_PAIRS -> combinationRate = 3;
            case PAIR -> combinationRate = 2;
            case HIGH_CARD -> {
            }
        }
        return combinationRate;
    }


    public void startGame() {
        dealCardsToTheAllPlayers();
        for (int i = 0; i < 5; i++) {
            cardsOnTheTable.add(takeCardFromTheDeck());
        }
        init();
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

    public void getWinnerByHighCard(Set<Player> players) {
        Set<Player> tempWinnerSet = new HashSet<>();
        players.forEach(player -> player.getCombinationCards().sort(Comparator.comparing(Card::getNumber).reversed()));
        if (players.size() == 1) {
            winnerList = new HashSet<>();
            winnerList.addAll(players);
            return;
        }
        int highCard = -1;
        for (Player currentPlayer : players) {
            if (currentPlayer.getCombinationCards().isEmpty()) {
                winnerList = new HashSet<>();
                winnerList.addAll(players);
                return;
            }
            if (highCard < currentPlayer.getCombinationCards().get(0).getNumber()) {
                tempWinnerSet = new HashSet<>();
                tempWinnerSet.add(currentPlayer);
                highCard = currentPlayer.getCombinationCards().get(0).getNumber();
            }
            if (highCard <= currentPlayer.getCombinationCards().get(0).getNumber()) {
                currentPlayer.getCombinationCards().remove(0);
                tempWinnerSet.add(currentPlayer);
            }
        }
        if (!tempWinnerSet.isEmpty()) {
            getWinnerByHighCard(tempWinnerSet);
        }
    }

    public void getWinnerByXOfKind(Set<Player> players) {
        Set<Player> tempWinnerList = new HashSet<>();
        int highestCardNumber = -1;
        int secondHighestCardNumber = -1;
        for (Player player : players) {
            int tempFirstPair = -1;
            int tempSecondPair = -1;
            Map<Integer, Long> cardsMap = player.getCombinationCards().stream()
                    .collect(Collectors.groupingBy(Card::getNumber, Collectors.counting()));
            for (Map.Entry<Integer, Long> entry : cardsMap.entrySet()) {
                if (entry.getValue() == 4) {
                    if (entry.getKey() > highestCardNumber) {
                        tempWinnerList = new HashSet<>();
                        tempWinnerList.add(player);
                    } else if (entry.getKey() == highestCardNumber) {
                        tempWinnerList.add(player);
                    }
                } else if (entry.getValue() == 3) {
                    if (player.getCombination().equals(Combination.FULL_HOUSE)) {
                        if (entry.getKey() > highestCardNumber) {
                            highestCardNumber = entry.getKey();
                            highestCombination = player.getCombination();
                            tempWinnerList = new HashSet<>();
                            tempWinnerList.add(player);
                        } else if (entry.getKey() == highestCardNumber) {
                            tempWinnerList.add(player);
                            player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                    .filter(c -> c.getNumber() != entry.getKey()).toList());
                            hasSameCards = true;
                        }
                    } else {
                        if (entry.getKey() > highestCardNumber) {
                            tempWinnerList = new HashSet<>();
                            tempWinnerList.add(player);
                        } else if (entry.getKey() == highestCardNumber) {
                            tempWinnerList.add(player);
                        }
                    }
                } else if (entry.getValue() == 2) {
                    if (player.getCombination().equals(Combination.FULL_HOUSE)) {
                        if (entry.getKey() > secondHighestCardNumber) {
                            secondHighestCardNumber = entry.getKey();
                            tempWinnerList = new HashSet<>();
                            tempWinnerList.add(player);
                        } else if (entry.getKey() == highestCardNumber) {
                            tempWinnerList.add(player);
                            player.getCombinationCards().removeAll(player.getCombinationCards().stream()
                                    .filter(c -> c.getNumber() == entry.getKey()).toList());
                            hasSameCards = true;
                        }
                    } else if (player.getCombination().equals(Combination.TWO_PAIRS)) {
                        if (tempFirstPair == -1) {
                            tempFirstPair = entry.getKey();
                        } else {
                            tempSecondPair = entry.getKey();
                        }
                        if (tempFirstPair > tempSecondPair) {
                            if (tempFirstPair > highestCardNumber) {
                                highestCardNumber = tempFirstPair;
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                                if (tempSecondPair > secondHighestCardNumber) {
                                    secondHighestCardNumber = tempSecondPair;
                                }
                            }
                        } else if (tempSecondPair > tempFirstPair) {
                            if (tempSecondPair > highestCardNumber) {
                                highestCardNumber = tempSecondPair;
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                                if (tempFirstPair > secondHighestCardNumber) {
                                    secondHighestCardNumber = tempFirstPair;
                                }
                            }
                        }
                        if (tempFirstPair == highestCardNumber && tempSecondPair == secondHighestCardNumber){
                            tempWinnerList.add(player);
                        }
                        List<Card> cardsToRemove = player.getCombinationCards().stream()
                                .filter(c -> c.getNumber() == entry.getKey()).toList();
                        player.getCombinationCards().removeAll(cardsToRemove);
                        hasSameCards = true;


//                            if (entry.getKey() > highestCardNumber || entry.getKey() > secondHighestCardNumber) {
//                                highestCardNumber = entry.getKey();
//                                tempWinnerList = new HashSet<>();
//                                tempWinnerList.add(player);
//                            }
//                            if (entry.getKey() == highestCardNumber) {
//                                tempWinnerList.add(player);
//                                List<Card> cardsToRemove = player.getCombinationCards().stream()
//                                        .filter(c -> c.getNumber() == entry.getKey()).toList();
//                                player.getCombinationCards().removeAll(cardsToRemove);
//                                hasSameCards = true;
//                            }
                        }
                    else
                            if (entry.getKey() > highestCardNumber) {
                                highestCardNumber = entry.getKey();
                                tempWinnerList = new HashSet<>();
                                tempWinnerList.add(player);
                    }
                    if (entry.getKey() == highestCardNumber) {
                        tempWinnerList.add(player);
                        List<Card> cardsToRemove = player.getCombinationCards().stream()
                                .filter(c -> c.getNumber() == entry.getKey()).toList();
                        player.getCombinationCards().removeAll(cardsToRemove);
                        hasSameCards = true;
//                    }
//                        }
                    }
                }
            }
        }
        winnerList = tempWinnerList;
    }


}