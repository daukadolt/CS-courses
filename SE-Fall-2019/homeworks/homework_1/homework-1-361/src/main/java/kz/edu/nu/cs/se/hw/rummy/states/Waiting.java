package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

public class Waiting extends State {

    public Waiting(Rummy rummy) {
        this.rummy = rummy;
        this.state = Steps.WAITING;
    }

    @Override
    public int isFinished() {
        return -1;
    }

    @Override
    public void initialDeal() {
        int dealPerHand = 0;
        String[] players = rummy.getPlayers();
        int numPlayers = rummy.getPlayers().length;
        switch (numPlayers) {
            case 2:
                dealPerHand = 10;
                break;
            case 3:
            case 4:
                dealPerHand = 7;
                break;
            case 5:
            case 6:
                dealPerHand = 6;
                break;
            default:
                throw new RummyException("Invalid player number");
        }
        for(int i = 0, playerToDealTo = 0; i<dealPerHand*numPlayers; i++, playerToDealTo++, playerToDealTo%=numPlayers) {
            rummy.getPlayerHand(players[playerToDealTo]).add(rummy.gameTable.drawFromDeck());
        }
        String card = rummy.gameTable.drawFromDeck();
        rummy.gameTable.putToDiscard(card);

        rummy.setCurrentState(rummy.draw);

    }

    @Override
    public void drawFromDiscard() {
//        rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).add(rummy.gameTable.drawFromDeck());
        throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
    }

    @Override
    public void drawFromDeck() {
//        rummy.playerHands.get(rummy.players.get(rummy.currentPlayer)).add(rummy.gameTable.drawFromDeck());
        throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
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
        throw new RummyException("", RummyException.EXPECTED_MELD_STEP);
    }

    @Override
    public void shuffle(Long l) {

    }

    @Override
    public void rearrange(String card) {
        rummy.gameTable.rearrange(card);
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
