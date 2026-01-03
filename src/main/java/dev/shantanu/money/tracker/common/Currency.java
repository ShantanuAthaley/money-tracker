package dev.shantanu.money.tracker.common;

/**
 * Currency associated with the account.
 * Enum representation poses limitation on scaling or adding more currencies and requires code changes.
 * <p>For now limiting to only two currency ('$' is just for, meh!) for which this application is targeting.
 */
public enum Currency {
    INR('₹', "INR"),
    USD('$', "USD");
    private final Character symbol;
    private final String abbreviation;

    Currency(final Character symbol, final String abbreviation) {
        this.symbol = symbol;
        this.abbreviation = abbreviation;
    }

    public Character getSymbol() {
        return symbol;
    }
    public String getAbbreviation() {
        return abbreviation;
    }

    public Currency from(String abbreviation) {
        return valueOf(abbreviation.trim().toUpperCase());
    }
}
