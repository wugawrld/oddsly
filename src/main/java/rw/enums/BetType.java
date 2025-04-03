package rw.enums;

/**
 * Enum for different types of bets that can be placed.
 */

public enum BetType {
    MONEYLINE("Moneyline"),
    POINT_SPREAD("Point Spread"),
    OVER_UNDER("Over/Under");

    private final String displayName;

    BetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

     // Convert a numeric choice to the corresponding BetType.
    public static BetType fromChoice(int choice) {
        switch (choice) {
            case 1: return MONEYLINE;
            case 2: return POINT_SPREAD;
            case 3: return OVER_UNDER;
            default: return null;
        }
    }

    // Convert a display name to the corresponding BetType.
    public static BetType fromDisplayName(String displayName) {
        for (BetType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }
}