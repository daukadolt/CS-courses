package kz.edu.nu.cs.se.hw.rummy.states;

import kz.edu.nu.cs.se.hw.rummy.Rummy;
import kz.edu.nu.cs.se.hw.rummy.RummyException;

public abstract class State {

    protected Rummy rummy;

    protected Steps state;

    public  Steps step() {
        return this.state;
    }

    /**
     * Return the index of the winning player when the game is in the finished step.
     * Returns -1 if the game is not in the FINISHED step.
     *
     * @return int winning player
     */
    public abstract int isFinished();

    /**
     * Begins the game. Deals hands to all players. Sets the current player to 0
     * (<code>getCurrentPlayer() == 0</code> immediately after this method is
     * called). The game must be in the WAITING step to call this method.
     *
     * The number of cards dealt to each player depends on the number of players in
     * the game. For 2 players each receives 10 cards. For 3-4 players each receives
     * 7 cards. For 5-6 players each receives 6 cards.
     *
     * @throws RummyException
     */
    public abstract void initialDeal();

    /**
     * Current player draws the top card from the discard pile. The game must be in
     * the DRAW step.
     *
     * @throws RummyException
     * @see Steps
     */
    public abstract void drawFromDiscard();

    /**
     * Current player draws the top card from the deck. The game must be in the DRAW
     * step.
     *
     * Following the rules of Rummy, if the deck is exhausted when a player attempts
     * to draw then the discard pile should be turned over (not shuffled) and one
     * card should be drawn to form a new discard pile.
     *
     * @throws RummyException
     * @see Steps
     */
    public abstract void drawFromDeck();

    /**
     * Current player forms a meld with cards. cards must be a valid meld and the
     * player must have the cards in their hand.
     * The game must be in the MELD or RUMMY step.
     *
     * @param cards Card[] cards to form new meld
     * @throws RummyException
     */
    public abstract void meld(String... cards);

    /**
     * Current player adds cards to an existing meld. The parameter
     * <code>meldIndex</code> is a reference to a current meld (first, second,
     * third, up to number of current melds). <code>cards</code> must make a valid
     * meld with the existing meld and the player must have the cards in their hand.
     * The game must be in the MELD or RUMMY step.
     *
     * @param meldIndex int reference to a current meld
     * @param cards     Card[] cards to add to meld
     * @throws RummyException
     */
    public abstract void addToMeld(int meldIndex, String... cards);

    /**
     * The current player declares "rummy." The game must be in the MELD step. After
     * calling this method the game moves to the RUMMY step. While in the RUMMY
     * step, the player can form melds and put down cards. The game will persist in
     * the RUMMY step until the current player has at most 1 card remaining. Or calls
     * the <code>finishMeld</code> method.
     *
     * A player can only declare rummy if they have not put down or melded cards.
     *
     * @throws RummyException
     */
    public abstract void declareRummy();

    /**
     * Finish the MELD step on the current players turn. The game must be in the
     * MELD or RUMMY step. After calling this method the game moves to the DISCARD
     * step.
     *
     * Raises an exception if the player has not demonstrated rummy.
     *
     * @throws RummyException
     */
    public abstract void finishMeld();

    /**
     * The current player discards <code>card</code> to the discard pile. The game
     * must be in the DISCARD step. If this call succeeds the game moves to the next
     * player's turn.
     *
     * The player may not discard a card drawn from the discard pile on the same
     * turn.
     *
     * @param card Card card to discard
     * @throws RummyException
     */
    public abstract void discard(String card);

    /**
     * Move specified card to the top of the deck before play begins. The game must
     * be in WAITING step to call this method.
     *
     * @param card String card to find
     */
    public abstract void rearrange(String card);

    /**
     * Shuffle the deck before play begins. The long parameter is a seed value for
     * an instance of <code>java.util.Random</code>. The game must be in the WAITING
     * step to call this method.
     *
     * @param l long random seed value
     * @throws RummyException
     * @see java.util.Random
     */
    public abstract void shuffle(Long l);
}
