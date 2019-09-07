package kz.edu.nu.cs.se.hw;

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

    public Rummy(String... players) {
    }

    @Override
    public String[] getPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumPlayers() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getCurrentPlayer() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumCardsInDeck() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumCardsInDiscardPile() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getTopCardOfDiscardPile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getHandOfPlayer(int player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumMelds() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String[] getMeld(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rearrange(String card) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shuffle(Long l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Steps getCurrentStep() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int isFinished() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void initialDeal() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drawFromDiscard() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drawFromDeck() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void meld(String... cards) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void declareRummy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void finishMeld() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void discard(String card) {
        // TODO Auto-generated method stub
        
    }
    
    

}
