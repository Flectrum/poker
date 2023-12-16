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

        assertEquals(46, hand.getDeck().getCards().size());
    }

    public void testDealCardsToThePlayer() {
    }

    public void testCheckForXOfKindCombination() {
    }

    public void testCheckForFullHouseAndTwoPairs() {
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Diamonds");
        Card card2 = new Card(8, "Clubs");
        Card card3 = new Card(1, "Clubs");
        Card card4 = new Card(9, "Spades");
        Card card5 = new Card(9, "Diamonds");
        Card card6 = new Card(8, "Hearts");
        Card card7 = new Card(13, "Clubs");
        Card card8 = new Card(2, "Clubs");
        Card card9 = new Card(2, "Hearts");
        Card card10 = new Card(9, "Hearts");
        Card card11 = new Card(3, "Hearts");


        player1.setCards(new Card[]{card1, card2});
        player2.setCards(new Card[]{card8, card9});
        player3.setCards(new Card[]{card10, card11});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForXOfKindCombination(player1);
        hand.checkForXOfKindCombination(player2);
        hand.checkForXOfKindCombination(player3);

        System.out.println(player1.getCombinationCards().size());
        System.out.println(player2.getCombinationCards().size());
        System.out.println(player3.getCombinationCards().size());

        assertEquals(Combination.FULL_HOUSE, player1.getCombination1());
        assertEquals(Combination.TWO_PAIRS, player2.getCombination1());
        assertEquals(Combination.THREE_OF_KIND, player3.getCombination1());
    }

    public void testCheckForStraight() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10, "Clubs");
        Card card3 = new Card(11, "Clubs");
        Card card4 = new Card(12, "Spades");
        Card card5 = new Card(8, "Spades");
        Card card6 = new Card(12, "Clubs");
        Card card7 = new Card(13, "Clubs");


        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForStraight(player1, false);

        assertEquals(Combination.STRAIGHT, player1.getCombination1());
    }


    public void testCheckForStraightFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(1, "Clubs");
        Card card2 = new Card(2, "Clubs");
        Card card3 = new Card(3, "Clubs");
        Card card4 = new Card(4, "Clubs");
        Card card5 = new Card(13, "Clubs");
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

        assertEquals(Combination.STRAIGHT_FLUSH, player1.getCombination1());
    }

    public void testCheckForRoyalFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10, "Clubs");
        Card card3 = new Card(11, "Clubs");
        Card card4 = new Card(12, "Clubs");
        Card card5 = new Card(13, "Clubs");
        Card card6 = new Card(12, "Hearts");
        Card card7 = new Card(13, "Diamonds");

        player1.setCards(new Card[]{card1, card2});

        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForFlush(player1);
        hand.checkForStraight(player1, false);

        assertEquals(Combination.ROYAL_FLUSH, player1.getCombination1());
    }

    public void testCheckForFlush() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(1, "Diamonds");
        Card card2 = new Card(6, "Clubs");
        Card card3 = new Card(8, "Clubs");
        Card card4 = new Card(9, "Clubs");
        Card card5 = new Card(2, "Clubs");
        Card card6 = new Card(10, "Diamonds");
        Card card7 = new Card(13, "Clubs");

        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.checkForFlush(player1);

        assertEquals(Combination.FLUSH, player1.getCombination1());
    }

    public void testFindHighCard() {
    }


    public void testGetStraightHighCard() {
    }

    public void testGetDeck() {
    }

    public void testSetCardsOnTheTable() {
    }

//    public void testGetWinnerByHighCard() {
//        players = new ArrayList<>();
//        player1 = new Player();
//        player1.setName("Alex");
//        player2 = new Player();
//        player2.setName("Nata");
//        cardsOnTheTable = new ArrayList<>();
//        players.add(player1);
//        players.add(player2);
//        hand = new Hand(players);
//
//        Card card1 = new Card(3, "Diamonds");
//        Card card2 = new Card(4, "Clubs");
//        Card card3 = new Card(10, "Diamonds");
//        Card card4 = new Card(11, "Spades");
//        Card card5 = new Card(12, "Spades");
//        Card card6 = new Card(2, "Clubs");
//        Card card7 = new Card(1, "Clubs");
//        Card card8 = new Card(4, "Hearts");
//        Card card9 = new Card(2, "Hearts");
//
//
//        player1.setCards(new Card[]{card1, card2});
//        player2.setCards(new Card[]{card8, card9});
//        cardsOnTheTable.add(card3);
//        cardsOnTheTable.add(card4);
//        cardsOnTheTable.add(card5);
//        cardsOnTheTable.add(card6);
//        cardsOnTheTable.add(card7);
//
//        hand.setCardsOnTheTable(cardsOnTheTable);
//
//        hand.setHighCards(player1, 5);
//        hand.setHighCards(player2, 5);
//
//        hand.getWinnerByHighCard(players);
//
//        assertEquals("Alex", hand.getWinner().getName());
//    }

    public void testGetWinnerByHighCardSplitTrue() {
        players = new ArrayList<>();
        player1 = new Player();
        player1.setName("Alex");
        player2 = new Player();
        player2.setName("Nata");
        cardsOnTheTable = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        hand = new Hand(players);

        Card card1 = new Card(3, "Diamonds");
        Card card2 = new Card(4, "Clubs");
        Card card3 = new Card(10, "Diamonds");
        Card card4 = new Card(11, "Spades");
        Card card5 = new Card(12, "Spades");
        Card card6 = new Card(2, "Clubs");
        Card card7 = new Card(1, "Clubs");
        Card card8 = new Card(4, "Hearts");
        Card card9 = new Card(3, "Hearts");


        player1.setCards(new Card[]{card1, card2});
        player2.setCards(new Card[]{card8, card9});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        hand.setHighCards(player1, 5);
        hand.setHighCards(player2, 5);

        hand.getWinnerByHighCard(players);

        assertTrue(hand.isSplit());
    }
}