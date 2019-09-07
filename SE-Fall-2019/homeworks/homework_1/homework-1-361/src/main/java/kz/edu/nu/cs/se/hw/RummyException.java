package kz.edu.nu.cs.se.hw;

/**
 * Exceptions for <code>PlayableRummy</code>. The constants below list
 * documented kinds of exceptional and error cases that an implementation may
 * encounter.
 * 
 * Using the methods that return state information, clients of the
 * <code>PlayableRummy</code> API should be able to avoid programming errors. .
 * However, clients should also be able to recover from these exceptional cases.
 * 
 * @see PlayableRummy
 * @see TestRummyCode
 */
public class RummyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final int KIND_NOT_SET = 0;
    public static final int NOT_VALID_MELD = 1;
    public static final int NOT_ENOUGH_PLAYERS = 2;
    public static final int EXPECTED_WAITING_STEP = 3;
    public static final int EXPECTED_DRAW_STEP = 4;
    public static final int EXPECTED_MELD_STEP = 5;
    public static final int EXPECTED_DISCARD_STEP = 6;
    public static final int EXPECTED_CARDS = 7;
    public static final int EXPECTED_FEWER_PLAYERS = 8;
    public static final int NOT_VALID_CARD_DESCRIPTION = 9;
    public static final int NOT_VALID_INDEX_OF_PLAYER = 10;
    public static final int NOT_VALID_INDEX_OF_MELD = 11;
    public static final int EXPECTED_FINISHED_STEP = 12;
    public static final int NOT_VALID_DISCARD = 13;
    public static final int EXPECTED_RUMMY_STEP = 14;
    public static final int EXPECTED_MELD_STEP_OR_RUMMY_STEP = 15;
    public static final int RUMMY_NOT_DEMONSTRATED = 16;

    /**
     * The kind of error that caused a <code>RummyException</code> to be raised.
     */
    public final int kind;

    public RummyException() {
        super();

        this.kind = RummyException.KIND_NOT_SET;
    }

    public RummyException(String message) {
        super(message);

        this.kind = RummyException.KIND_NOT_SET;
    }

    public RummyException(String message, int kind) {
        super(message);

        this.kind = kind;
    }
}
