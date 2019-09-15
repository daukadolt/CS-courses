package kz.edu.nu.cs.se.hw.rummy;

import kz.edu.nu.cs.se.hw.rummy.states.Steps;

import java.util.*;

public class GameTable {
    final String[] suits = new String[] { "C", "D", "H", "S", "M" };
    final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    public Stack<String> deck, discardPile;
    public ArrayList<Set<String>> melds;
    public String lastDrawnDiscardedCard;
    public GameTable() {
        deck = new Stack<>();
        discardPile = new Stack<>();
        melds = new ArrayList<>();
        lastDrawnDiscardedCard = "";
        for(String suit: suits) {
            for(String rank: ranks) {
                deck.push(rank+suit);
            }
        }
    }

    public int getNumCardsInDeck() {
        // TODO Auto-generated method stub
        return deck.size();
    }

    public int getNumCardsInDiscardPile() {
        // TODO Auto-generated method stub
        return discardPile.size();
    }

    public String getTopCardOfDiscardPile() throws RummyException {
        // TODO Auto-generated method stub
        return discardPile.peek();
    }

    public List<String> getDeck() {
        return deck;
    }

    public int getNumMelds() {
        return melds.size();
    }

    public String[] getMeld(int i) {
        if(melds.size() == 0 || i<0 || i>=melds.size()) {
            throw new RummyException("", RummyException.NOT_VALID_INDEX_OF_MELD);
        }
        return melds.get(i).toArray(new String[0]);
    }

    public void rearrange(String card) {
        // TODO Auto-generated method stub
        for(int i = 0; i<deck.size(); i++) {
            String deckCard = deck.get(i);
            if(deckCard.equals(card)) {
                deck.remove(i);
                deck.add(deckCard);
                break;
            }
        }
    }

    public void shuffle(Long l) {
        Collections.shuffle(deck, new Random(l));
    }

    public String drawFromDeck() {
        String card;
        if(deck.size() == 1) {
            card = deck.pop();
            deck.addAll(discardPile);
            discardPile.clear();
        } else {
            card = deck.pop();
        }
        return card;
    }

    public String drawFromDiscard() {
        lastDrawnDiscardedCard = discardPile.peek();
        return discardPile.pop();
    }

    public void putToDiscard(String card) {
        discardPile.push(card);
    }
}
