package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

import java.util.*;

public class Meld extends State {

    public Meld(Rummy rummy) {
        this.rummy = rummy;
        this.state = Steps.MELD;
    }

    @Override
    public int isFinished() {
        return -1;
    }

    @Override
    public void initialDeal() {
        throw new RummyException("Not in the waiting state", RummyException.EXPECTED_WAITING_STEP);
    }

    @Override
    public void drawFromDiscard() {
        throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
    }

    @Override
    public void drawFromDeck() {
        throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
    }

    @Override
    public void meld(String... cards) {
        if(cards.length < 3) throw new RummyException("", RummyException.NOT_VALID_MELD);
        String player = rummy.players.get(rummy.currentPlayer);
        for(String card: cards) {
            if(!rummy.playerHands.get(player).contains(card)) {
                throw new RummyException("", RummyException.EXPECTED_CARDS);
            }
        }
        ArrayList<String> respectiveRanks = new ArrayList<>();
        ArrayList<String> respectiveSuits = new ArrayList<>();
        for(String card: cards) {
            int div = card.length() == 3 ? 2 : 1;
            String rank = card.substring(0, div);
            String suit = card.substring(div, card.length());
            respectiveRanks.add(rank);
            respectiveSuits.add(suit);
        }
        boolean equalRanks = true;
        for(String rank: respectiveRanks) {
            if(!rank.equals(respectiveRanks.get(0))) {
                equalRanks = false;
                break;
            }
        }
        if(equalRanks) {
            if( cards.length != 3 && cards.length != 4 && cards.length != 5) throw new RummyException("", RummyException.NOT_VALID_MELD);
            for(String card: cards) {
                rummy.playerHands.get(player).remove(card);
            }
            if(rummy.playerHands.get(player).size() == 0) {
                rummy.setCurrentState(rummy.finished);
            }
            Set<String> lastMeld = new HashSet<>(Arrays.asList(cards));
            rummy.gameTable.melds.add(lastMeld);
            return;
        }
        boolean sameSuits = true;
        for(String suit: respectiveSuits) {
            if(!suit.equals(respectiveSuits.get(0))) {
                sameSuits = false;
                break;
            }
        }
        Map<String, Integer> rankToInt = new HashMap<>();
        rankToInt.put("A", 1);
        rankToInt.put("J", 11);
        rankToInt.put("Q", 12);
        rankToInt.put("K", 13);
        ArrayList<Integer> cardsToInts = new ArrayList<>();
        for(String rank: respectiveRanks) {
            if(!rankToInt.containsKey(rank)) {
                cardsToInts.add(Integer.parseInt(rank));
            } else {
                cardsToInts.add(rankToInt.get(rank));
            }
        }
        for(int i = 1; i<cardsToInts.size(); i++) {
            if(cardsToInts.get(i)-cardsToInts.get(i-1) != 1) {
                throw new RummyException("", RummyException.NOT_VALID_MELD);
            }
        }
        for(String card: cards) {
            rummy.playerHands.get(player).remove(card);
        }
        if(rummy.playerHands.get(player).size() == 0) {
            rummy.setCurrentState(rummy.finished);
        }
        Set<String> lastMeld = new HashSet<>(Arrays.asList(cards));
        rummy.gameTable.melds.add(lastMeld);
        return;

    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        for(String card: cards) {
            rummy.gameTable.melds.get(meldIndex).add(card);
            rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).remove(card);
        }
        if(rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).size() == 0) {
            rummy.winner = rummy.currentPlayer;
            rummy.setCurrentState(rummy.finished);
        }
    }

    @Override
    public void declareRummy() {
        rummy.setCurrentState(rummy.rummy);
    }

    @Override
    public void shuffle(Long l) {
        throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
    }

    @Override
    public void rearrange(String card) {
        throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
    }

    @Override
    public void finishMeld() {
        rummy.setCurrentState(rummy.discard);
    }

    @Override
    public void discard(String card) {
        throw new RummyException("", RummyException.EXPECTED_DISCARD_STEP);
    }
}
