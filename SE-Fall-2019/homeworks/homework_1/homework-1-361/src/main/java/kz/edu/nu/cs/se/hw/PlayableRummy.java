package kz.edu.nu.cs.se.hw;

/**
 * An interface for the game logic of a variation of the popular card game
 * "rummy". Together with the unit tests <code>TestRummyCode</code>, an
 * implementation of this interface is meant to be an exercise in
 * object-oriented development. An implementation of this type should provide a
 * constructor with the signature <code>Rummy(String... players)</code> where
 * the elements of <code>players</code> are the names of the players.
 * <p>
 * The expected behavior of an implementation is outlined in the unit tests that
 * have been provided. The methods of this interface are only concerned with
 * gameplay. There are no methods for dealing with scores. The first player to
 * discard their hand, consistent with the rules, wins the game. The possible
 * states of the game depend on the current player and the various phases of the
 * play as described in the enum <code>Steps</code>.
 * <p>
 * Improper uses for this type are intended to be handled with unchecked
 * exceptions as described in <code>RummyException</code>.
 * <p>
 * The accompanying unit tests assume certain conventions. A <b>non-standard</b>
 * deck of 65 playing cards with 5 suits and 13 ranks is assumed. The 5 suits
 * are Clubs, Diamonds, Hearts, Spades, and Moons. Individual cards are
 * referenced by short 2-3 character strings. The first group of characters
 * indicates rank while the last group indicates the suit. For example, the
 * string <code>"3D"</code> would represent the 3 of Diamonds. The string
 * <code>"10C"</code> represents the 10 of Clubs. The regular expression
 * <code>"(A|2|3|4|5|6|7|8|9|10|J|Q|K)(C|D|H|S|M)"</code> defines valid card
 * descriptions. To give some more examples, <code>"AM"</code> represents the
 * Ace of Moons, <code>"2D"</code> represents the Two of Diamonds,
 * <code>"QH"</code> represents the Queen of Hearts. When forming runs, an Ace
 * can be treated as high or low. For example, <code>AC,2C,3C</code> and
 * <code>JC,QC,KC,AC</code> are both to be understood as valid runs.
 * <p>
 * The order of the players should be consistent with the order seen in the
 * array returned from <code>getPlayers()</code>. The first element of the array
 * is the name of the first player, second of the second player and so on. Play
 * progresses and cards are dealt in the corresponding order. Play always begins
 * with the first player.
 * <p>
 * A newly instantiated instance of this type should always be in the WAITING
 * step. The methods <code>rearrange</code> and <code>shuffle</code> are meant
 * to allow the client to put the deck into a certain state before play begins.
 * The method <code>initialDeal</code> deals cards to the players and moves the
 * game to the first turn of the first player in the DRAW step. Play continues
 * until a player discards or melds all of their cards. The game should go to
 * the FINISHED step when that happens.
 * 
 * @see TestRummyCode
 * @see Steps
 * @see RummyException
 * @see java.util.Random
 */
public interface PlayableRummy {

    /**
     * Returns a <code>String</code> array containing the names of the players.
     * 
     * @return String[]
     */
    public String[] getPlayers();

    /**
     * Get the number of players for this game.
     * 
     * @return int
     */
    public int getNumPlayers();

    /**
     * Get an index representing the current player. Should be consistent with the
     * result returned from <code>getPlayers</code>.
     * 
     * @return int
     */
    public int getCurrentPlayer();

    /**
     * Get the number of cards currently in the deck.
     * 
     * @return int
     */
    public int getNumCardsInDeck();

    /**
     * Get the number of cards currently in the discard pile.
     * 
     * @return int
     */
    public int getNumCardsInDiscardPile();

    /**
     * Return a string description of the top of the discard pile.
     * 
     * @return String
     * @throws RummyException
     */
    public String getTopCardOfDiscardPile();

    /**
     * Return a String array showing the cards held by <code>player</code>.
     * 
     * @param player int a reference to a player in game
     * @return String[]
     * @throws RummyException
     */
    public String[] getHandOfPlayer(int player);

    /**
     * Get the current number of melds.
     * 
     * @return int
     */
    public int getNumMelds();

    /**
     * Return a String array showing the cards in the meld specified by
     * <code>i</code>. Order of the melds matter, and an implementation of this
     * interface is expected to maintain the melds in the order in which they were
     * placed down.
     * 
     * @param i int index of meld
     * @return String[]
     * @throws RummyException
     */
    public String[] getMeld(int i);

    /**
     * Move specified card to the top of the deck before play begins. The game must
     * be in WAITING step to call this method.
     * 
     * @param card String card to find
     */
    public void rearrange(String card);

    /**
     * Shuffle the deck before play begins. The long parameter is a seed value for
     * an instance of <code>java.util.Random</code>. The game must be in the WAITING
     * step to call this method.
     * 
     * @param l long random seed value
     * @throws RummyException
     * @see java.util.Random
     */
    public void shuffle(Long l);

    /**
     * Return the step of the current game.
     * 
     * @see Steps
     * @return Steps
     */
    public Steps getCurrentStep();

    /**
     * Return the index of the winning player when the game is in the finished step.
     * Returns -1 if the game is not in the FINISHED step.
     * 
     * @return int winning player
     */
    public int isFinished();

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
    public void initialDeal();

    /**
     * Current player draws the top card from the discard pile. The game must be in
     * the DRAW step.
     * 
     * @throws RummyException
     * @see Steps
     */
    public void drawFromDiscard();

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
    public void drawFromDeck();

    /**
     * Current player forms a meld with cards. cards must be a valid meld and the
     * player must have the cards in their hand. 
     * The game must be in the MELD or RUMMY step.
     * 
     * @param cards Card[] cards to form new meld
     * @throws RummyException
     */
    public void meld(String... cards);

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
    public void addToMeld(int meldIndex, String... cards);

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
    public void declareRummy();

    /**
     * Finish the MELD step on the current players turn. The game must be in the
     * MELD or RUMMY step. After calling this method the game moves to the DISCARD
     * step.
     * 
     * Raises an exception if the player has not demonstrated rummy.  
     * 
     * @throws RummyException
     */
    public void finishMeld();

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
    public void discard(String card);
}
