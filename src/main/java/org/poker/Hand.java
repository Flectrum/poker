package org.poker;

import java.util.ArrayList;
import java.util.List;

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
            CombinationChecker combinationChecker = new CombinationChecker(player, cardsOnTheTable);
            combinationChecker.check();
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
            }
        }
        getWinnerByHighCard(winners);
    }

    private boolean hasFullHouse(Player player){
        return player.getCombination().equals(Combination.FULL_HOUSE);
    }

    public  int cardNumber(Player player){
        int i = -1;
        int count = 1;
        for(Card card: player.getCombinationCards()){
            if(card.getNumber() > i){
                i = card.getNumber();
            } else if (card.getNumber() == i){
                count++;
                if((count == 3  && hasFullHouse(player))){
                    return card.getNumber();
                }
            }
        }
        return 0;
    }
}


// Full House - x = combination.stream().filter(count == 3 ), if x == x, pair, else - split

// Two Pair - Comparator.compare(combination.getNumber()), combination.get(size -1). else combination.get(0), else - highCard
