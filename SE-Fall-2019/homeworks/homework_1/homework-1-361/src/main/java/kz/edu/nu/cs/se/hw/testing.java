package kz.edu.nu.cs.se.hw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class testing {

    public static final String[] suits = new String[] { "C", "D", "H", "S", "M" };
    public static final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    private static ArrayList<String> deck;

    public static void main(String[] args) {
        testing test = new testing();
        System.out.println(test.deck);
        Collections.shuffle(test.deck, new Random(1L));
        System.out.println(test.deck);
        test.rearrange("2M");
        test.rearrange("AM");
        test.rearrange("2S");
        test.rearrange("AS");
        test.rearrange("2H");
        test.rearrange("AH");
        test.rearrange("2D");
        test.rearrange("AD");
        test.rearrange("2C");
        test.rearrange("AC");
        System.out.println(test.deck);
    }

    public testing() {
        deck = new ArrayList<>();
        for(String suit: suits) {
            for(String rank: ranks) {
                deck.add(rank+suit);
            }
        }
//        Collections.reverse(deck);
    }

    public void rearrange(String card) {
        for(int i = 0; i<deck.size(); i++) {
            String deckCard = deck.get(i);
            if(deckCard.equals(card)) {
//                System.out.println("Before removing " + card);
//                System.out.println(deck);
                deck.remove(i);
                deck.add(card);
//                System.out.println("After removing " + card);
//                System.out.println(deck);
                break;
            }
        }
    }
}
