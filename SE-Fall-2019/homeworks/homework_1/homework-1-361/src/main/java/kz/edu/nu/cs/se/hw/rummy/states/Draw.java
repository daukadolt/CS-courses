package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

public class Draw extends State {

    public Draw(Rummy rummy) {
        this.rummy = rummy;
        this.state = Steps.DRAW;
    }

    @Override
    public Steps step() {
        return state;
    }

    @Override
    public int isFinished() {
        return -1;
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
    public void initialDeal() {
        throw new RummyException("Not in the waiting state", RummyException.EXPECTED_WAITING_STEP);
    }

    @Override
    public void drawFromDiscard() {
        rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).add(rummy.gameTable.drawFromDiscard());
        rummy.setCurrentState(rummy.meld);
    }

    @Override
    public void drawFromDeck() {
        rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).add(rummy.gameTable.drawFromDeck());
        rummy.setCurrentState(rummy.meld);
    }

    @Override
    public void meld(String... cards) {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
    }

    @Override
    public void declareRummy() {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
    }

    @Override
    public void finishMeld() {
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
    }

    @Override
    public void discard(String card) {
        throw new RummyException("", RummyException.EXPECTED_DISCARD_STEP);
    }
}
