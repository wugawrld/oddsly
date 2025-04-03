package rw.enums;

/**
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
