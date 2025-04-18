package rw.enums;

/**
 * CPSC 233 Project UserInput class
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.cra, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 * Enum for possible outcomes of a bet.
 */

public enum BetOutcome {
    WIN("win"),
    LOSS("loss"),
    PENDING("pending");

    private final String displayName;

    BetOutcome(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Convert a numeric choice to the corresponding BetOutcome.
    public static BetOutcome fromChoice(int choice) {
        switch (choice) {
            case 1: return WIN;
            case 2: return LOSS;
            default: return PENDING;
        }
    }
    // Convert a display name to the corresponding BetOutcome.
    public static BetOutcome fromDisplayName(String displayName) {
        for (BetOutcome outcome : values()) {
            if (outcome.displayName.equalsIgnoreCase(displayName)) {
                return outcome;
            }
        }
        return PENDING;
    }
}
