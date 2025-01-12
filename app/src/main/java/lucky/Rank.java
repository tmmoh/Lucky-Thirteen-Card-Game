package lucky;

public enum Rank {
    // Reverse order of rank importance (see rankGreater() below)
    ACE (1, 1, 0, 1),
    KING (13, 13, 10, 11, 12, 13),
    QUEEN (12, 12, 10, 11, 12, 13),
    JACK (11, 11, 10, 11, 12, 13),
    TEN (10, 10, 10), NINE (9, 9, 9),
    EIGHT (8, 8, 8), SEVEN (7, 7, 7),
    SIX (6, 6, 6), FIVE (5, 5, 5),
    FOUR (4, 4, 4), THREE (3, 3, 3),
    TWO (2, 2, 2);

    private int value = 0;
    private int []possibleSumValues = null;
    Rank(int value, int... possibleSumValues) {
        this.value = value;
        this.possibleSumValues = possibleSumValues;
    }

    public int getRankCardValue() {
        return value;
    }

    public int getScoreCardValue() { return value; }

    public int[] getPossibleSumValues() {
        return possibleSumValues;
    }

    public static Rank fromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    public String getRankCardLog() {
        return String.format("%d", value);
    }
}
