package kz.edu.nu.cs.se.hw.rummy;

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
    private ArrayList<String> players;
    private int currentPlayer;
    private ArrayList<String> deck, discardPile;
    private Map<String, ArrayList<String>> playerHands;
    private Steps state;
    private int winner;
    private Set<String> lastDrawnDiscardedCard;
    private ArrayList<Set<String>> melds;
    final String[] suits = new String[] { "C", "D", "H", "S", "M" };
    final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

    public Rummy(String... players) {
        if(players.length < 2) {
            throw new RummyException("", RummyException.NOT_ENOUGH_PLAYERS);
        } else if(players.length > 6) {
            throw new RummyException("", RummyException.EXPECTED_FEWER_PLAYERS);
        }
        this.players = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.playerHands = new TreeMap<>();
        this.lastDrawnDiscardedCard = new HashSet<>();
        this.players.addAll(Arrays.asList(players));
        for(String player: players) {
            this.playerHands.put(player, new ArrayList<>());
        }
        this.currentPlayer = 0;
        this.state = Steps.WAITING;
        this.deck = new ArrayList<>();
        this.melds = new ArrayList<>();
//        for(String rank: ranks) {
//            for(String suit: suits) {
//                deck.add(rank+suit);
//            }
//        }
        for(String suit: suits) {
            for(String rank: ranks) {
                System.out.println(rank+suit);
                deck.add(rank+suit);
            }
        }
    }

    public static void main(String[] args) {}

    @Override
    public String[] getPlayers() {
        // TODO Auto-generated method stub
        return players.toArray(new String[0]);
    }

    public List<String> getDeck() {
        return deck;
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
        System.out.println("deckSize "+deck.size());
        return deck.size();
    }

    @Override
    public int getNumCardsInDiscardPile() {
        // TODO Auto-generated method stub
        System.out.println("discard size "+discardPile.size());
        return discardPile.size();
    }

    @Override
    public String getTopCardOfDiscardPile() throws RummyException {
        // TODO Auto-generated method stub
        System.out.println("TopCardOfDiscardPile, size = " + discardPile.size());
        if(discardPile.size() == 0) throw new RummyException("Pile is empty");
        return discardPile.get(discardPile.size()-1);
    }

    @Override
    public String[] getHandOfPlayer(int player) throws RummyException {
        // TODO Auto-generated method stub
        if(player < 0 || player >= players.size()) throw new RummyException("", RummyException.NOT_VALID_INDEX_OF_PLAYER);
        if(playerHands.get(players.get(player)).size() == 0) throw new RummyException("Player's hand is empty");
        System.out.println("Player hand " + playerHands.get(players.get(player)));
        return playerHands.get(players.get(player)).toArray(new String[0]);
    }

    @Override
    public int getNumMelds() {
        // TODO Auto-generated method stub
        System.out.println("Melds size: " + this.melds.size());
        return melds.size();
    }

    @Override
    public String[] getMeld(int i) {
        // TODO Auto-generated method stub
        if(melds.size() == 0 || i<0 || i>=melds.size()) {
            throw new RummyException("", RummyException.NOT_VALID_INDEX_OF_MELD);
        }
        return melds.get(i).toArray(new String[0]);
    }

    @Override
    public void rearrange(String card) {
        // TODO Auto-generated method stub
        if(this.state != Steps.WAITING) throw new RummyException("", RummyException.EXPECTED_WAITING_STEP);
        for(int i = 0; i<deck.size(); i++) {
            String deckCard = deck.get(i);
            if(deckCard.equals(card)) {
                deck.remove(i);
                deck.add(deckCard);
//                    System.out.println(deck);
                break;
            }
        }
    }

    @Override
    public void shuffle(Long l) {
        // TODO Auto-generated method stub
        if (this.state != Steps.WAITING) throw new RummyException("Not in the waiting state", RummyException.EXPECTED_WAITING_STEP);
        System.out.println("Before shuffle");
        System.out.println(deck);
        Collections.shuffle(deck, new Random(l));
        System.out.println("After shuffle");
        System.out.println(deck);
    }

    @Override
    public Steps getCurrentStep() {
        // TODO Auto-generated method stub
        return this.state;
    }

    @Override
    public int isFinished() {
        // TODO Auto-generated method stub
        System.out.println("Finished: " + this.state + "Winner " + this.winner);
        return this.state == Steps.FINISHED ? this.winner : -1;
    }

    @Override
    public void initialDeal() {
        // TODO Auto-generated method stub
        if(this.state != Steps.WAITING) throw new RummyException("Not in the waiting state", RummyException.EXPECTED_WAITING_STEP);
        int dealPerHand = 0;
        switch (this.players.size()) {
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
        for(int i = 0, playerToDealTo = 0; i<dealPerHand*players.size(); i++, playerToDealTo++, playerToDealTo%=players.size()) {
            playerHands.get(players.get(playerToDealTo)).add(this.deck.get(this.deck.size()-1));
            this.deck.remove(this.deck.size()-1);
        }
        String card = this.deck.get(this.deck.size()-1);
        this.deck.remove(this.deck.size()-1);
        this.discardPile.add(card);

        this.state = Steps.DRAW;


    }

    @Override
    public void drawFromDiscard() {
        // TODO Auto-generated method stub
        System.out.println("drawFromDiscard");
        if(this.state != Steps.DRAW) throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
        String card = this.discardPile.get(this.discardPile.size()-1);
        this.discardPile.remove(this.discardPile.size()-1);
        playerHands.get(players.get(currentPlayer)).add(card);
        lastDrawnDiscardedCard.add(card);
        this.state = Steps.MELD;
    }

    @Override
    public void drawFromDeck() {
        // TODO Auto-generated method stub
        System.out.println("drawFromDeck, state = " + this.state);
        if(this.state != Steps.DRAW) throw new RummyException("", RummyException.EXPECTED_DRAW_STEP);
        if(deck.size() == 1) {
            String card = deck.get(deck.size()-1);
            deck.remove(deck.size()-1);
            deck.addAll(discardPile);
            discardPile.clear();
        } else {
            String card = deck.get(deck.size()-1);
            deck.remove(deck.size()-1);
            playerHands.get(players.get(currentPlayer)).add(card);
        }
        this.state = Steps.MELD;
    }

    @Override
    public void meld(String... cards) {
        // TODO Auto-generated method stub
        System.out.println("Melding with player " + players.get(currentPlayer));
        System.out.println("Melds: " + this.melds);
        if(this.state == Steps.MELD || this.state == Steps.RUMMY) {
            if(cards.length < 3) throw new RummyException("", RummyException.NOT_VALID_MELD);
            String player = players.get(currentPlayer);
            for(String card: cards) {
                if(!playerHands.get(player).contains(card)) {
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
                if(cards.length != 4 && cards.length != 5) throw new RummyException("", RummyException.NOT_VALID_MELD);
                for(String card: cards) {
                    playerHands.get(player).remove(card);
                }
                if(playerHands.get(player).size() == 0) {
                    this.state = Steps.FINISHED;
                }
                Set<String> lastMeld = new HashSet<>(Arrays.asList(cards));
                this.melds.add(lastMeld);
                return;
            }
            // Not equal ranks, then check for identity of suits
            for(String suit: respectiveSuits) {
                if(!suit.equals(respectiveRanks.get(0))) {
                    System.out.println("This thrown?");
                    throw new RummyException("", RummyException.NOT_VALID_MELD);
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
                if(cardsToInts.get(i) - cardsToInts.get(i-1) != 1) {
                    throw new RummyException("", RummyException.NOT_VALID_MELD);
                }
            }
            for(String card: cards) {
                playerHands.get(player).remove(card);
            }
            Set<String> lastMeld = new HashSet<>(Arrays.asList(cards));
            this.melds.add(lastMeld);
            return;
        } else {
            throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
        }

    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void declareRummy() {
        // TODO Auto-generated method stub
        if(this.state != Steps.MELD) throw new RummyException("", RummyException.EXPECTED_MELD_STEP);
        this.state = Steps.RUMMY;
    }

    @Override
    public void finishMeld() {
        // TODO Auto-generated method stub
        String player = players.get(currentPlayer);
        if(this.state == Steps.RUMMY && playerHands.get(player).size() != 0) throw new RummyException("", RummyException.RUMMY_NOT_DEMONSTRATED);
        System.out.println("finishMeld");
        System.out.println("Melds: " + this.melds);
        if(this.state == Steps.MELD || this.state == Steps.RUMMY) {
            System.out.println("Stuff");
            this.state = Steps.DISCARD;
        } else {
            System.out.println("finishMeld");
            throw new RummyException();
        }
        
    }

    @Override
    public void discard(String card) {
        // TODO Auto-generated method stub
        if(!(this.state == Steps.DISCARD || this.state == Steps.RUMMY)) throw new RummyException("", RummyException.EXPECTED_DISCARD_STEP);
        String player = players.get(currentPlayer);
        System.out.println("Player " + player + " hand is : " + playerHands.get(player));
        if(playerHands.get(player).contains(card) && lastDrawnDiscardedCard.contains(card)) {
            throw new RummyException("", RummyException.NOT_VALID_DISCARD);
        }
        if(!playerHands.get(player).contains(card)) throw new RummyException("", RummyException.EXPECTED_CARDS);
        if(playerHands.get(player).size() == 1) {
            playerHands.get(player).remove(card);
            discardPile.add(card);
            winner = currentPlayer;
            this.state = Steps.FINISHED;
            return;
        }
        System.out.println("Current player is " + (currentPlayer+1));
        System.out.println("Deck size = " + this.deck.size());
        System.out.println("Discard size = " + this.discardPile.size());
        System.out.println("Hand size = " + playerHands.get(player).size());
        System.out.println("Card to discard is " + card);
        System.out.println("Hand is " + playerHands.get(player));
        for(int i = 0; i<playerHands.get(player).size(); i++) {
            if(playerHands.get(player).get(i).equals(card)) {
                playerHands.get(player).remove(i);
                discardPile.add(card);
                currentPlayer++;
                currentPlayer %= players.size();
                System.out.println("Player changed to "+ currentPlayer);
                break;
            }
        }
        this.state = Steps.DRAW;
    }
    
    

}
