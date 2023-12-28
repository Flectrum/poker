package org.poker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        for(int i = 0; i < 1000; i++){
            Player player1 = new Player();
            player1.setName("Alex");

            Player player2 = new Player();
            player2.setName("Nata");

            Player player3 = new Player();
            player3.setName("Igor");

            Set<Player> playerList =  new HashSet<>();
            playerList.add(player1);
            playerList.add(player2);
            playerList.add(player3);

            Hand hand = new Hand(playerList);

            hand.startGame();

            System.out.println(hand.getCardsOnTheTable());
            System.out.println(player1.getName() + " " +player1.getCombination() + " " + Arrays.toString(player1.getCards()));
            System.out.println(player2.getName() + " " +player2.getCombination() + " " + Arrays.toString(player2.getCards()));
            System.out.println(player3.getName() + " " + player3.getCombination() + " " + Arrays.toString(player3.getCards()));

            System.out.println(hand.getWinnerList().size());
            if(hand.isSplit()){
                System.out.println("SPLIT");
                for(Player player: hand.getWinnerList()){
                    System.out.print(player.getName() +" ");
                    System.out.println();
                }
            } else {
                hand.getWinnerList().forEach(p -> System.out.println(p.getName() + " " + p.getCombination()));
            }
            System.out.println("_______________________________");
        }

    }
}
