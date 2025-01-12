package lucky;

public enum Suit {
    SPADES("S", 4), HEARTS("H", 3),
    DIAMONDS("D", 2), CLUBS("C", 1);
    private final String suitShortHand;
    private final int multiplicationFactor;
    public static final int PUBLIC_CARD_MULTIPLICATION_FACTOR = 2;

    Suit(String shortHand, int multiplicationFactor) {
        this.suitShortHand = shortHand;
        this.multiplicationFactor = multiplicationFactor;
    }

    public String getSuitShortHand() {
        return suitShortHand;
    }

    public int getMultiplicationFactor() {
        return multiplicationFactor;
    }

    public static Suit fromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1, cardName.length());

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }
}
