package kz.edu.nu.cs.se.hw;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

public class TestRummyCode {

    /*
     * Helper methods for running tests
     */
    public static PlayableRummy create(String... players) {

        // Edit this line to test with implementations with different names
        return new Rummy(players);
    }

    public static void shuffle_1(PlayableRummy rummy) {
        final String[] suits = new String[] { "C", "D", "H", "S", "M" };
        final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
        for (String suit : suits) {
            for (String rank : ranks) {
                rummy.rearrange(rank + suit);
            }
        }
    }

    /*
     * Test cases
     * 
     */

    @Test
    public void test_number_cards_in_deck_1() {
        PlayableRummy rummy = create("Alice", "Bob");

        shuffle_1(rummy);

        assertTrue("Cards in deck after insantiation", rummy.getNumCardsInDeck() == 65);
    }

    @Test
    public void test_number_cards_in_deck_2() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();

        assertTrue("Cards in deck after deal, 2 player game", rummy.getNumCardsInDeck() == 44);
    }

    @Test
    public void test_number_cards_in_deck_3() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire");
        rummy.initialDeal();

        rummy.drawFromDeck();
        rummy.finishMeld();
        rummy.discard(rummy.getHandOfPlayer(rummy.getCurrentPlayer())[0]);

        rummy.drawFromDeck();

        assertTrue("Cards in deck after play, 3 player game", rummy.getNumCardsInDeck() == 41);
    }

    @Test
    public void test_number_melds_1() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();

        assertTrue("No melds immediately after deal", rummy.getNumMelds() == 0);
    }

    @Test
    public void test_long_game() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire", "Darren");
        
        shuffle_1(rummy);
        
        rummy.initialDeal();

        for (int i = 0; i < 38; i++) {
            rummy.drawFromDeck();
            rummy.finishMeld();
            rummy.discard(rummy.getHandOfPlayer(rummy.getCurrentPlayer())[0]);
        }
        
        assertTrue("Long game, player", rummy.getCurrentPlayer() == 2);
        assertTrue("Long game, cards in deck", rummy.getNumCardsInDeck() == 34);
        assertTrue("Long game, top card of discard", rummy.getTopCardOfDiscardPile().equals("2H"));
    }

    @Test
    public void test_number_of_players() {
        assertTrue("Two player game", create("Alice", "Bob").getNumPlayers() == 2);
        assertTrue("Five player game", create("Alice", "Bob", "Claire", "Darren", "Elaine").getNumPlayers() == 5);
        assertTrue("Six player game", create("Alice", "Bob", "Claire", "Darren", "Elaine", "Felix").getNumPlayers() == 6);
    }

    @Test
    public void test_current_player() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire", "Darren", "Elaine");

        rummy.shuffle(1L);

        rummy.initialDeal();

        for (int i = 0; i < 32; i++) {
            rummy.drawFromDeck();
            rummy.finishMeld();
            rummy.discard(rummy.getHandOfPlayer(rummy.getCurrentPlayer())[0]);
        }

        assertTrue("Current player", rummy.getPlayers()[rummy.getCurrentPlayer()].equals("Claire"));
    }

    @Test
    public void test_current_step() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire");

        assertTrue("Test current step waiting, turn 1", rummy.getCurrentStep() == Steps.WAITING);
        rummy.initialDeal();
        assertTrue("Test current step draw, turn 1", rummy.getCurrentStep() == Steps.DRAW);
        rummy.drawFromDeck();
        assertTrue("Test current step meld, turn 1", rummy.getCurrentStep() == Steps.MELD);
        rummy.finishMeld();
        assertTrue("Test current step discard, turn 1", rummy.getCurrentStep() == Steps.DISCARD);
        rummy.discard(rummy.getHandOfPlayer(rummy.getCurrentPlayer())[0]);
        assertTrue("Test current step draw, turn 2", rummy.getCurrentStep() == Steps.DRAW);
        rummy.drawFromDeck();
        assertTrue("Test current step meld, turn 2", rummy.getCurrentStep() == Steps.MELD);
    }

    @Test
    public void test_rearrange() {
        PlayableRummy rummy = create("Alice", "Bob");
        shuffle_1(rummy);

        rummy.initialDeal();

        String[] h1 = rummy.getHandOfPlayer(0);
        String[] h2 = rummy.getHandOfPlayer(1);
        Arrays.sort(h1);
        Arrays.sort(h2);

        assertTrue("shuffle_1 cards of first player", h1[0].equals("10M") && h1[1].equals("2M"));
        assertTrue("shuffle_1 cards of second player", h2[0].equals("10S") && h2[1].equals("3M"));
    }

    /*
     * Exceptional test cases
     * 
     */

    @Test
    public void test_meld_exception_1() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire");

        rummy.initialDeal();
        rummy.drawFromDeck();

        try {
            rummy.meld("AC", "4C", "7C");
            fail("Meld exception 1");
        } catch (RummyException e) {
            assertTrue("Throws exception for not valid meld", e.kind == RummyException.NOT_VALID_MELD);
        }
    }

    @Test
    public void test_game_insantiation_1() {
        try {
            create("Alice");
            fail("Game instantiation 1");
        } catch (RummyException e) {
            assertTrue("Throws exception for too few players", e.kind == RummyException.NOT_ENOUGH_PLAYERS);
        }
    }

    @Test
    public void test_game_insantiation_2() {
        try {
            create("Alice", "Bob", "Claire", "Dennis", "Elaine", "Felix", "Grace");
            fail("Game insantiation 2");
        } catch (RummyException e) {
            assertTrue("Throws exception for more than 6 players", e.kind == RummyException.EXPECTED_FEWER_PLAYERS);
        }
    }

    @Test
    public void test_discard_rule() {
        try {
            PlayableRummy rummy = create("Alice", "Bob", "Claire");
            rummy.initialDeal();

            String discard = rummy.getTopCardOfDiscardPile();
            rummy.drawFromDiscard();
            rummy.finishMeld();
            rummy.discard(discard);
            fail("Test discard rule");
        } catch (RummyException e) {
            assertTrue("Discard same card from discard", e.kind == RummyException.NOT_VALID_DISCARD);
        }
    }

    @Test
    public void test_initial_deal_waiting() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();
        rummy.drawFromDiscard();

        try {
            rummy.initialDeal();
            fail("Test initial deal waiting");
        } catch (RummyException e) {
            assertTrue("Deal after game begins", e.kind == RummyException.EXPECTED_WAITING_STEP);
        }

    }

    @Test
    public void test_rearrange_call() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();

        try {
            rummy.rearrange("10M");
            fail("Test rearrange call");
        } catch (RummyException e) {
            assertTrue("Called rearrange in draw step", e.kind == RummyException.EXPECTED_WAITING_STEP);
        }
    }

    @Test
    public void test_shuffle_call() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();

        try {
            rummy.shuffle(20L);
            fail("Test shuffle call");
        } catch (RummyException e) {
            assertTrue("Called shuffle in draw step", e.kind == RummyException.EXPECTED_WAITING_STEP);
        }
    }

    @Test
    public void test_draw_from_deck_call() {
        PlayableRummy rummy = create("Alice", "Bob");

        try {
            rummy.drawFromDeck();
            fail("Test draw from deck");
        } catch (RummyException e) {
            assertTrue("Called draw from deck in waiting step", e.kind == RummyException.EXPECTED_DRAW_STEP);
        }
    }

    @Test
    public void test_draw_from_discard_call() {
        PlayableRummy rummy = create("Alice", "Bob");

        try {
            rummy.drawFromDiscard();
            fail("Test draw from discard");
        } catch (RummyException e) {
            assertTrue("Called draw from discard in waiting step", e.kind == RummyException.EXPECTED_DRAW_STEP);
        }
    }

    @Test
    public void test_get_hand_out_of_bounds() {
        PlayableRummy rummy = create("Alice", "Bob", "Claire");
        rummy.initialDeal();

        try {
            rummy.getHandOfPlayer(3);
            fail("Test get hand out of bounds");
        } catch (RummyException e) {
            assertTrue("Called get hand with index out of range", e.kind == RummyException.NOT_VALID_INDEX_OF_PLAYER);
        }
    }

    @Test
    public void test_get_meld_out_of_bounds() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.initialDeal();

        try {
            rummy.getMeld(0);
            fail("Test get meld out of bonds");
        } catch (RummyException e) {
            assertTrue("Called get meld with index out of range", e.kind == RummyException.NOT_VALID_INDEX_OF_MELD);
        }
    }

    @Test
    public void test_meld_call_exception() {
        PlayableRummy rummy = create("Alice", "Bob");

        rummy.rearrange("9C");
        rummy.rearrange("4C");
        rummy.rearrange("8C");
        rummy.rearrange("3C");
        rummy.rearrange("7C");
        rummy.rearrange("2C");

        rummy.initialDeal();

        try {
            rummy.meld("2C", "3C", "4C");
            fail("Called meld in draw step");
        } catch (RummyException e) {
            assertTrue("Meld call exception", e.kind == RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
        }
    }

    @Test
    public void test_declare_rummy_call() {
        PlayableRummy rummy = create("Alice", "Bob");

        try {
            rummy.declareRummy();
            fail("Test declare rummy call");
        } catch (RummyException e) {
            assertTrue("Called declare rummy in waiting step", e.kind == RummyException.EXPECTED_MELD_STEP);
        }

    }

    @Test
    public void test_discard_call() {
        PlayableRummy rummy = create("Alice", "Bob");

        rummy.rearrange("AC");

        rummy.initialDeal();
        rummy.drawFromDeck();

        try {
            rummy.discard("AC");
            fail("Test discard call");
        } catch (RummyException e) {
            assertTrue("Discard called during meld step", e.kind == RummyException.EXPECTED_DISCARD_STEP);
        }

    }

    @Test
    public void test_discard_mistake() {
        PlayableRummy rummy = create("Alice", "Bob");

        shuffle_1(rummy);

        rummy.initialDeal();
        rummy.drawFromDeck();
        rummy.finishMeld();

        try {
            rummy.discard("3D");
            fail("Test discard call");
        } catch (RummyException e) {
            assertTrue("Tried to discard but the current player does not have card",
                    e.kind == RummyException.EXPECTED_CARDS);
        }

    }

    /*
     * Sample games
     * 
     */

    @Test
    public void game_1() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(1L);

        rummy.rearrange("2M");
        rummy.rearrange("AM");
        rummy.rearrange("2S");
        rummy.rearrange("AS");
        rummy.rearrange("2H");
        rummy.rearrange("AH");
        rummy.rearrange("2D");
        rummy.rearrange("AD");
        rummy.rearrange("2C");
        rummy.rearrange("AC");

        rummy.initialDeal();
        
        // turn 1
        rummy.drawFromDeck();
        rummy.meld("AD", "AH", "AS", "AC", "AM");
        rummy.finishMeld();
        rummy.discard("QH");

        assertTrue("Game 1, turn 1, melds", rummy.getNumMelds() == 1);
        assertTrue("Game 1, turn 1, deck", rummy.getNumCardsInDeck() == 43);
        assertTrue("Game 1, turn 1, discard", rummy.getTopCardOfDiscardPile().equals("QH"));
        assertTrue("Game 1, turn 1, meld", rummy.getMeld(0).length == 5);
    }

    @Test
    public void game_2() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(1L);

        rummy.rearrange("4D");
        rummy.rearrange("4H");
        rummy.rearrange("4S");
        rummy.rearrange("4C");
        rummy.rearrange("7M");
        rummy.rearrange("6M");
        rummy.rearrange("7S");
        rummy.rearrange("6S");
        rummy.rearrange("7H");
        rummy.rearrange("6H");
        rummy.rearrange("7D");
        rummy.rearrange("6D");
        rummy.rearrange("7C");
        rummy.rearrange("6C");
        rummy.rearrange("2M");
        rummy.rearrange("AM");
        rummy.rearrange("2S");
        rummy.rearrange("AS");
        rummy.rearrange("2H");
        rummy.rearrange("AH");
        rummy.rearrange("2D");
        rummy.rearrange("AD");
        rummy.rearrange("2C");
        rummy.rearrange("AC");

        rummy.initialDeal();
        
        // turn 1
        rummy.drawFromDeck();
        rummy.meld("AD", "AH", "AS", "AC", "AM");
        rummy.meld("6D", "6H", "6S", "6C", "6M");
        rummy.finishMeld();
        rummy.discard("4S");

        assertTrue("Game 2, turn 1, melds", rummy.getNumMelds() == 2);
        assertTrue("Game 2, turn 1, deck", rummy.getNumCardsInDeck() == 43);
        assertTrue("Game 2, turn 1, discard", rummy.getNumCardsInDiscardPile() == 2);
        assertTrue("Game 2, turn 1, meld", rummy.getMeld(1).length == 5);
        assertTrue("Game 2, turn 1, finished", rummy.isFinished() == 0);
    }

    @Test
    public void game_3() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(1L);

        rummy.rearrange("4D");
        rummy.rearrange("10H");
        rummy.rearrange("4S");
        rummy.rearrange("4C");
        rummy.rearrange("7M");
        rummy.rearrange("6M");
        rummy.rearrange("7S");
        rummy.rearrange("6S");
        rummy.rearrange("7H");
        rummy.rearrange("6H");
        rummy.rearrange("7D");
        rummy.rearrange("6D");
        rummy.rearrange("7C");
        rummy.rearrange("6C");
        rummy.rearrange("2M");
        rummy.rearrange("AM");
        rummy.rearrange("2S");
        rummy.rearrange("AS");
        rummy.rearrange("2H");
        rummy.rearrange("AH");
        rummy.rearrange("2D");
        rummy.rearrange("AD");
        rummy.rearrange("2C");
        rummy.rearrange("AC");

        rummy.initialDeal();
        
        // turn 1
        rummy.drawFromDeck();
        rummy.meld("AD", "AH", "AS", "AC", "AM");
        rummy.finishMeld();
        rummy.discard("4S");
        
        // turn 2
        rummy.drawFromDeck();
        rummy.meld("2C", "2D", "2H", "2S", "2M");
        rummy.meld("7C", "7D", "7H", "7S", "7M");
        rummy.finishMeld();
        rummy.discard("10H");

        assertTrue("Game 3, turn 2, melds", rummy.getNumMelds() == 3);
        assertTrue("Game 3, turn 2, deck", rummy.getNumCardsInDeck() == 42);
        assertTrue("Game 3, turn 2, discard", rummy.getNumCardsInDiscardPile() == 3);
        assertTrue("Game 3, turn 2, meld", rummy.getMeld(2).length == 5);
        assertTrue("Game 3, turn 2, finished", rummy.isFinished() == 1);
    }

    @Test
    public void game_4() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(2L);

        rummy.rearrange("4D");
        rummy.rearrange("4H");
        rummy.rearrange("4S");
        rummy.rearrange("4C");
        rummy.rearrange("7M");
        rummy.rearrange("6M");
        rummy.rearrange("7S");
        rummy.rearrange("6S");
        rummy.rearrange("7H");
        rummy.rearrange("6H");
        rummy.rearrange("7D");
        rummy.rearrange("6D");
        rummy.rearrange("7C");
        rummy.rearrange("6C");
        rummy.rearrange("2M");
        rummy.rearrange("AM");
        rummy.rearrange("2S");
        rummy.rearrange("AS");
        rummy.rearrange("2H");
        rummy.rearrange("AH");
        rummy.rearrange("2D");
        rummy.rearrange("AD");
        rummy.rearrange("2C");
        rummy.rearrange("AC");

        rummy.initialDeal();

        // turn 1
        rummy.drawFromDeck();
        rummy.declareRummy();
        assertTrue("Game 4, turn 1, rummy declared", rummy.getCurrentStep() == Steps.RUMMY);
        rummy.meld("AC", "AD", "AH", "AS", "AM");
        try {
            rummy.meld("6C", "6D", "6H", "6S", "10M");
            fail("Game 4, Expected Exception from Wrong Card");
        } catch (RummyException e) {
            assertTrue(e.kind == RummyException.EXPECTED_CARDS);
        }
        try {
            rummy.finishMeld();
            fail("Game 4, Expected Exception, Tried to finish rummy step without discarding all cards");
        } catch (RummyException e) {
            assertTrue(e.kind == RummyException.RUMMY_NOT_DEMONSTRATED);
        }
        rummy.discard("4S");
        
        // turn 2
        rummy.drawFromDeck();
        rummy.declareRummy();
        rummy.meld("7C", "7D", "7H", "7S", "7M");
        rummy.meld("2C", "2D", "2H", "2S", "2M");
        
        assertTrue("Game 4, turn 2, successful rummy", rummy.getCurrentStep() == Steps.FINISHED);
        assertTrue("Game 4, turn 2, melds", rummy.getNumMelds() == 3);
        assertTrue("Game 4, turn 2, finished", rummy.isFinished() == 1);
    }

    @Test
    public void game_5() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(2L);

        rummy.rearrange("4D");
        rummy.rearrange("4H");
        rummy.rearrange("4S");
        rummy.rearrange("4C");
        rummy.rearrange("7M");
        rummy.rearrange("6M");
        rummy.rearrange("7S");
        rummy.rearrange("6S");
        rummy.rearrange("7H");
        rummy.rearrange("6H");
        rummy.rearrange("7D");
        rummy.rearrange("6D");
        rummy.rearrange("7C");
        rummy.rearrange("6C");
        rummy.rearrange("2M");
        rummy.rearrange("AM");
        rummy.rearrange("2S");
        rummy.rearrange("AS");
        rummy.rearrange("2H");
        rummy.rearrange("AH");
        rummy.rearrange("2D");
        rummy.rearrange("AD");
        rummy.rearrange("2C");
        rummy.rearrange("AC");

        rummy.initialDeal();

        // turn 1
        rummy.drawFromDeck();
        rummy.meld("AC", "AD", "AH");
        rummy.finishMeld();
        rummy.discard("6D");
        
        // turn 2
        rummy.drawFromDeck();
        rummy.meld("2C", "2D", "2H");
        rummy.meld("7H", "7S", "7M");
        rummy.finishMeld();
        rummy.discard("4H");

        // turn 3
        rummy.drawFromDiscard();
        rummy.addToMeld(0, "AS", "AM");
        rummy.finishMeld();
        rummy.discard("4S");
        
        // turn 4
        rummy.drawFromDeck();
        rummy.addToMeld(1, "2S", "2M");
        rummy.addToMeld(2, "7C", "7D");
        rummy.finishMeld();
        rummy.discard("4D");

        assertTrue("Game 5, turn 4, successful rummy", rummy.getCurrentStep() == Steps.FINISHED);
        assertTrue("Game 5, turn 4, melds", rummy.getNumMelds() == 3);
        assertTrue("Game 5, turn 4, discard", rummy.getTopCardOfDiscardPile().equals("4D"));
        assertTrue("Game 5, turn 4, finished", rummy.isFinished() == 1);
    }

    @Test
    public void game_6() {
        PlayableRummy rummy = create("Alice", "Bob");
        rummy.shuffle(1L);

        rummy.rearrange("4D");
        rummy.rearrange("4H");
        rummy.rearrange("4S");
        rummy.rearrange("JC");
        rummy.rearrange("JD");
        rummy.rearrange("10C");
        rummy.rearrange("10D");
        rummy.rearrange("9C");
        rummy.rearrange("9D");
        rummy.rearrange("8C");
        rummy.rearrange("8D");
        rummy.rearrange("7C");
        rummy.rearrange("7D");
        rummy.rearrange("6C");
        rummy.rearrange("6D");
        rummy.rearrange("5C");
        rummy.rearrange("5D");
        rummy.rearrange("4C");
        rummy.rearrange("4D");
        rummy.rearrange("3C");
        rummy.rearrange("2H");
        rummy.rearrange("2C");
        rummy.rearrange("2D");
        rummy.rearrange("AC");

        rummy.initialDeal();
        
        // turn 1
        rummy.drawFromDiscard();
        rummy.meld("AC", "2C", "3C", "4C", "5C", "6C");
        rummy.addToMeld(0, "7C", "8C", "9C", "10C", "JC");

        assertTrue("Game 6, turn 1, melds", rummy.getNumMelds() == 1);
        assertTrue("Game 6, turn 1, deck", rummy.getNumCardsInDeck() == 44);
        assertTrue("Game 6, turn 1, meld", rummy.getMeld(0).length == 11);
        assertTrue("Game 6, turn 1, finished", rummy.isFinished() == 0);

    }
}
