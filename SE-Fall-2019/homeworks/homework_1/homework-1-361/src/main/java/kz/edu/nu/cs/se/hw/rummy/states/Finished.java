package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

public class Finished extends State {

    public Finished(Rummy rummy) {
        this.rummy = rummy;
        this.state = Steps.FINISHED;
    }

    @Override
    public int isFinished() {
        return rummy.winner;
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
    public void shuffle(Long l) {
        throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
    }

    @Override
    public void rearrange(String card) {
        throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
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
