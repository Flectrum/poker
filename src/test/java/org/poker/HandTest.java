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
    CombinationChecker combinationChecker;

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
        players = new ArrayList<>();

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

        players.add(player1);
        players.add(player2);
        players.add(player3);

        hand.setCardsOnTheTable(cardsOnTheTable);

        for(Player player: players){
            combinationChecker = new CombinationChecker(player, cardsOnTheTable);
            combinationChecker.check();
        }

        assertEquals(Combination.FULL_HOUSE, player1.getCombination());
        assertEquals(Combination.TWO_PAIRS, player2.getCombination());
        assertEquals(Combination.THREE_OF_KIND, player3.getCombination());
    }

    public void testCheckForStraight() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Clubs");
        Card card2 = new Card(10, "Diamonds");
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

        combinationChecker = new CombinationChecker(player1, cardsOnTheTable);
        combinationChecker.check();

        assertEquals(Combination.STRAIGHT, player1.getCombination());
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

        combinationChecker = new CombinationChecker(player1, cardsOnTheTable);
        combinationChecker.check();

        assertEquals(Combination.STRAIGHT_FLUSH, player1.getCombination());
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

        combinationChecker = new CombinationChecker(player1, cardsOnTheTable);
        combinationChecker.check();

        assertEquals(Combination.ROYAL_FLUSH, player1.getCombination());
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

        combinationChecker = new CombinationChecker(player1, cardsOnTheTable);
        combinationChecker.check();

        assertEquals(Combination.FLUSH, player1.getCombination());
    }

    public void testCheckForFullHouse() {
        player1 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(2, "Diamonds");
        Card card2 = new Card(2, "Clubs");
        Card card3 = new Card(8, "Clubs");
        Card card4 = new Card(3, "Clubs");
        Card card5 = new Card(3, "Clubs");
        Card card6 = new Card(3, "Diamonds");
        Card card7 = new Card(13, "Clubs");

        player1.setCards(new Card[]{card1, card2});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        combinationChecker = new CombinationChecker(player1, cardsOnTheTable);
        combinationChecker.check();

        assertEquals(Combination.FLUSH, player1.getCombination());
    }

//    public void testGetWinnerWithFourOfKindOnTheTableByHighCard() {
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
//        Card card1 = new Card(11, "Diamonds");
//        Card card2 = new Card(2, "Clubs");
//        Card card3 = new Card(6, "Diamonds");
//        Card card4 = new Card(7, "Spades");
//        Card card5 = new Card(7, "Spades");
//        Card card6 = new Card(7, "Clubs");
//        Card card7 = new Card(7, "Clubs");
//        Card card8 = new Card(3, "Hearts");
//        Card card9 = new Card(13, "Hearts");
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
//        for(Player player: players){
//            combinationChecker = new CombinationChecker(player, cardsOnTheTable);
//            combinationChecker.check();
//        }
//
//        assertEquals(Combination.FOUR_OF_KIND ,player1.getCombination());
//        assertEquals(Combination.FOUR_OF_KIND ,player2.getCombination());
//
//        hand.getWinnerByXOfKind(players);
//
//        assertEquals(1, hand.getWinnerList().size());
//        assertEquals("Nata", hand.getWinnerList().get(0).getName());
//    }
//
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
//        Card card1 = new Card(8, "Diamonds");
//        Card card2 = new Card(7, "Clubs");
//        Card card3 = new Card(10, "Diamonds");
//        Card card4 = new Card(11, "Spades");
//        Card card5 = new Card(12, "Spades");
//        Card card6 = new Card(2, "Clubs");
//        Card card7 = new Card(1, "Clubs");
//        Card card8 = new Card(9, "Hearts");
//        Card card9 = new Card(8, "Hearts");
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
//        hand.setHighCards(player1);
//        hand.setHighCards(player2);
//
//        hand.getWinnerByHighCard(players);
//
//        assertEquals("Nata", hand.getWinnerList().get(0).getName());
//    }
//
    public void testCheckForFullHouseThreeOfKindCardNumber() {
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Diamonds");
        Card card2 = new Card(9, "Clubs");
        Card card3 = new Card(3, "Clubs");
        Card card4 = new Card(9, "Spades");
        Card card5 = new Card(3, "Diamonds");
        Card card6 = new Card(3, "Hearts");
        Card card7 = new Card(4, "Clubs");
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

        CombinationChecker combinationChecker1 = new CombinationChecker(player1, cardsOnTheTable);
        CombinationChecker combinationChecker2 = new CombinationChecker(player2, cardsOnTheTable);
        CombinationChecker combinationChecker3 = new CombinationChecker(player3, cardsOnTheTable);

        combinationChecker1.check();
        combinationChecker2.check();
        combinationChecker3.check();

        assertEquals(Combination.FULL_HOUSE, player1.getCombination());
        assertEquals(Combination.FULL_HOUSE, player2.getCombination());
        assertEquals(Combination.FOUR_OF_KIND, player3.getCombination());

        players.add(player1);
        players.add(player2);
        players.add(player3);

        hand.getWinnerByXOfKind(players);

        assertEquals(1, hand.getWinnerList().size());
        assertEquals(player1, hand.getWinnerList().get(0));
        assertEquals(Combination.FULL_HOUSE, hand.getWinnerList().get(0).getCombination());

    }

    public void testCorrectCheckForThreePairsOnTheTable() {
        players = new ArrayList<>();
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        cardsOnTheTable = new ArrayList<>();

        Card card1 = new Card(9, "Diamonds");
        Card card2 = new Card(9, "Clubs");

        Card card3 = new Card(2, "Clubs");
        Card card4 = new Card(2, "Spades");
        Card card5 = new Card(1, "Diamonds");
        Card card6 = new Card(1, "Hearts");
        Card card7 = new Card(4, "Clubs");

        Card card8 = new Card(4, "Clubs");
        Card card9 = new Card(3, "Hearts");

        Card card10 = new Card(8, "Hearts");
        Card card11 = new Card(8, "Hearts");


        player1.setCards(new Card[]{card1, card2});
        player2.setCards(new Card[]{card8, card9});
        player3.setCards(new Card[]{card10, card11});
        cardsOnTheTable.add(card3);
        cardsOnTheTable.add(card4);
        cardsOnTheTable.add(card5);
        cardsOnTheTable.add(card6);
        cardsOnTheTable.add(card7);

        hand.setCardsOnTheTable(cardsOnTheTable);

        CombinationChecker combinationChecker1 = new CombinationChecker(player1, cardsOnTheTable);
        CombinationChecker combinationChecker2 = new CombinationChecker(player2, cardsOnTheTable);
        CombinationChecker combinationChecker3 = new CombinationChecker(player3, cardsOnTheTable);

        combinationChecker1.check();
        combinationChecker2.check();
        combinationChecker3.check();

        assertEquals(Combination.TWO_PAIRS, player1.getCombination());
        assertEquals(Combination.TWO_PAIRS, player2.getCombination());
        assertEquals(Combination.TWO_PAIRS, player3.getCombination());

        players.add(player1);
        players.add(player2);
        players.add(player3);

        hand.getWinnerByXOfKind(players);

        assertEquals(1, hand.getWinnerList().size());
        assertEquals(player1, hand.getWinnerList().get(0));
        assertEquals(Combination.TWO_PAIRS, hand.getWinnerList().get(0).getCombination());

    }
}