package org.poker;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class HandTest extends TestCase {

    Hand hand;
    Deck deck;
    Player player1;
    Player player2;
    Player player3;

    List<Card> cardsOnTheTable;

    List<Player> players;

    {
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();

        players = new ArrayList<>();

        players.add(player1);
        players.add(player2);
        players.add(player3);

        hand = new Hand(players);
    }
    public void testGetCardsOnTheTable() {
    }

    public void testGetWinner() {
    }

    public void testInit() {
    }

    public void testStartGame() {
    }

    public void testDealCardsToTheAllPlayers() {
        deck = new Deck();

        hand.dealCardsToTheAllPlayers();

        assertEquals( 46, hand.getDeck().getCards().size());
    }

    public void testDealCardsToThePlayer() {
        }

    public void testCheckForXOfKindCombination() {
    }

    public void testCheckForFullHouseAndTwoPairs() {
    }

    public void testCheckForStraight() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10,  "Clubs");
        Card card3 = new Card(11, "Clubs");
        Card card4 = new Card(12, "Spades");
        Card card5 = new Card(8, "Spades");
        Card card6 = new Card(12, "Clubs");
        Card card7 = new Card(8, "Clubs");


        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForStraight(player1, false);

        assertEquals("Straight", player1.getCombination());
    }

    public void testCheckForStraightFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10,  "Clubs");
        Card card3 = new Card(11, "Clubs");
        Card card4 = new Card(12, "Clubs");
        Card card5 = new Card(8, "Clubs");
        Card card6 = new Card(12, "Diamonds");
        Card card7 = new Card(8, "Spades");

        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForFlush(player1);
        hand.checkForStraight(player1, false);

        assertEquals("Straight Flush", player1.getCombination());
    }

    public void testCheckForRoyalFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10,  "Clubs");
        Card card3 = new Card(11, "Clubs");
        Card card4 = new Card(12, "Clubs");
        Card card5 = new Card(13, "Clubs");
//        Card card6 = new Card(12, "Hearts");
//        Card card7 = new Card(13, "Diamonds");

        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
//        cardsOnTheTable.add(card6);
//        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForFlush(player1);
        hand.checkForStraight(player1, false);

        assertEquals("Royal Flush", player1.getCombination());
    }
    public void testCheckForFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(1, "Clubs");
        Card card2 = new Card(6,  "Clubs");
        Card card3 = new Card(8, "Clubs");
        Card card4 = new Card(9, "Clubs");
        Card card5 = new Card(2, "Clubs");
        Card card6 = new Card(9, "Hearts");
        Card card7 = new Card(2, "Diamonds");

        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForFlush(player1);

        assertEquals("Flush", player1.getCombination());
    }

    public void testFindHighCard() {
    }

    public void testComparePlayersCombinations() {
    }

    public void testGetStraightHighCard() {
    }

    public void testGetDeck() {
    }

    public void testSetCardsOnTheTable() {
    }
}