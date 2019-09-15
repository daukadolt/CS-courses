package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

public class Discard extends State {

    public Discard(Rummy rummy) {
        this.rummy = rummy;
        this.state = Steps.DISCARD;
    }

    @Override
    public int isFinished() {
        return -1;
    }

    @Override
    public void initialDeal() {
        throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
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
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP);
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP);
    }

    @Override
    public void declareRummy() {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP);
    }

    @Override
    public void finishMeld() {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
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
    public void discard(String card) {
        String player = rummy.players.get(rummy.currentPlayer);
        if(rummy.playerHands.get(player).contains(card) && rummy.gameTable.lastDrawnDiscardedCard.equals(card)) {
            throw new RummyException("", RummyException.NOT_VALID_DISCARD);
        }
        if(!rummy.playerHands.get(player).contains(card)) throw new RummyException("", RummyException.EXPECTED_CARDS);
        if(rummy.playerHands.get(player).size() == 1) {
            rummy.playerHands.get(player).remove(card);
            rummy.gameTable.putToDiscard(card);
            rummy.winner = rummy.currentPlayer;
            rummy.setCurrentState(rummy.finished);
            return;
        }
        for(int i = 0; i<rummy.playerHands.get(player).size(); i++) {
            if(rummy.playerHands.get(player).get(i).equals(card)) {
                rummy.playerHands.get(player).remove(i);
                rummy.gameTable.putToDiscard(card);
                rummy.currentPlayer++;
                rummy.currentPlayer %= rummy.players.size();
                break;
            }
        }
        rummy.setCurrentState(rummy.draw);
    }
}
