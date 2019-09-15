package kz.edu.nu.cs.se.hw.rummy;

import kz.edu.nu.cs.se.hw.rummy.states.*;

import java.util.*;

/**
 * Starter code for a class that implements the <code>PlayableRummy</code>
 * interface. A constructor signature has been added, and method stubs have been
 * generated automatically in eclipse.
 * 
 * Before coding you should verify that you are able to run the accompanying
 * JUnit test suite <code>TestRummyCode</code>. Most of the unit tests will fail
 * initially.
 * 
 * @see PlayableRummy
 * @see TestRummyCode
 *
 */
public class Rummy implements PlayableRummy {
    public ArrayList<String> players;
    public int currentPlayer;
    public Map<String, ArrayList<String>> playerHands;
    public int winner;

    public State waiting = new Waiting(this);
    public State draw = new Draw(this);
    public State meld = new Meld(this);
    public State rummy = new RummyState(this);
    public State discard = new Discard(this);
    public State finished = new Finished(this);

    public State currentState;

    public GameTable gameTable;

    public Rummy(String... players) {
        if(players.length < 2) {
            throw new RummyException("", RummyException.NOT_ENOUGH_PLAYERS);
        } else if(players.length > 6) {
            throw new RummyException("", RummyException.EXPECTED_FEWER_PLAYERS);
        }
        this.players = new ArrayList<>();
        this.playerHands = new TreeMap<>();
        this.players.addAll(Arrays.asList(players));
        for(String player: players) {
            this.playerHands.put(player, new ArrayList<>());
        }
        this.currentPlayer = 0;
        this.currentState = waiting;
        this.gameTable = new GameTable();

    }


    @Override
    public String[] getPlayers() {
        // TODO Auto-generated method stub
        return players.toArray(new String[0]);
    }

    @Override
    public int getNumPlayers() {
        // TODO Auto-generated method stub
        return players.size();
    }

    @Override
    public int getCurrentPlayer() {
        // TODO Auto-generated method stub
        return currentPlayer;
    }

    @Override
    public int getNumCardsInDeck() {
        // TODO Auto-generated method stub
        return gameTable.getNumCardsInDeck();
    }

    @Override
    public int getNumCardsInDiscardPile() {
        // TODO Auto-generated method stub
        return gameTable.getNumCardsInDiscardPile();
    }

    @Override
    public String getTopCardOfDiscardPile() throws RummyException {
        // TODO Auto-generated method stub
        return gameTable.getTopCardOfDiscardPile();
    }

    @Override
    public String[] getHandOfPlayer(int player) throws RummyException {
        // TODO Auto-generated method stub
        if(player < 0 || player >= players.size()) throw new RummyException("", RummyException.NOT_VALID_INDEX_OF_PLAYER);
        return playerHands.get(players.get(player)).toArray(new String[0]);
    }

    @Override
    public int getNumMelds() {
        // TODO Auto-generated method stub
        return gameTable.getNumMelds();
    }

    @Override
    public String[] getMeld(int i) {
        // TODO Auto-generated method stub
        return gameTable.getMeld(i);
    }

    @Override
    public void rearrange(String card) {
        currentState.rearrange(card);
    }

    @Override
    public void shuffle(Long l) {
        // TODO Auto-generated method stub
        currentState.shuffle(l);
    }

    @Override
    public Steps getCurrentStep() {
        return currentState.step();
    }

    @Override
    public int isFinished() {
        // TODO Auto-generated method stub
        return currentState.isFinished();
    }

    @Override
    public void initialDeal() {
        // TODO Auto-generated method stub
        currentState.initialDeal();
    }

    public ArrayList<String> getPlayerHand(String player) {
        return playerHands.get(player);
    }

    public void setCurrentState(State newState) {
        currentState = newState;
    }

    @Override
    public void drawFromDiscard() {
        // TODO Auto-generated method stub
        currentState.drawFromDiscard();
    }

    @Override
    public void drawFromDeck() {
        // TODO Auto-generated method stub
        currentState.drawFromDeck();
    }

    @Override
    public void meld(String... cards) {
        // TODO Auto-generated method stub
        currentState.meld(cards);
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        // TODO Auto-generated method stub
        currentState.addToMeld(meldIndex, cards);
    }

    @Override
    public void declareRummy() {
        // TODO Auto-generated method stub
        currentState.declareRummy();
    }

    @Override
    public void finishMeld() {
        // TODO Auto-generated method stub
        currentState.finishMeld();
    }

    @Override
    public void discard(String card) {
        // TODO Auto-generated method stub
        currentState.discard(card);
    }
    
    

}
