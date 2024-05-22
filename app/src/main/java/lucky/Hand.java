package lucky;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.HandLayout;

import java.util.ArrayList;

public class Hand extends ch.aplu.jcardgame.Hand {
    public Hand(Deck deck) {
        super(deck);
    }

    public Hand(Deck deck, HandLayout handLayout) {
        super(deck, handLayout);
    }

    public void dealRandomCardToHand(Hand hand) {
        if (this.isEmpty()) {
            System.out.println("hand is empty: cannot deal");
            return;
        }

        Card dealt = getRandomCard();
        dealt.removeFromHand(true);
        hand.insert(dealt, true);
    }

    public Card getCard(String cardString) {
        Rank cardRank = Rank.fromString(cardString);
        Suit cardSuit = Suit.fromString(cardString);
        for (Card card : this.getCardList()) {
            if (card.getSuit() == cardSuit
                    && card.getRank() == cardRank) {
                return card;
            }
        }

        return null;
    }

    public void dealCardToHand(Hand hand, String cardString) {
        Card toDeal = getCard(cardString);

        if (toDeal != null) {
            toDeal.removeFromHand(true);
            hand.insert(toDeal, true);
        } else {
            System.out.println("cannot draw card: " + cardString + " - hand: " + hand);
            System.out.println("drawing randomly instead");
            dealRandomCardToHand(hand);
        }

    }

    public void discardRandomCard() {
        if (this.isEmpty()) {
            System.out.println("hand is empty: nothing to discard");
            return;
        }

        Card discarded = getRandomCard();
        discarded.removeFromHand(true);
    }

    public void discardCard(String cardString) {
        Card toDiscard = getCard(cardString);

        if (toDiscard != null) {
            toDiscard.removeFromHand(true);
        } else {
            System.out.println("cannot discard card: " + cardString + " - hand: " + this);
            System.out.println("discarding randomly instead");
            discardRandomCard();
        }
    }

    private Card getRandomCard() {
        ArrayList<Card> cardList = getCardList();
        int x = LuckyThirdteen.RANDOM.nextInt(cardList.size());
        return cardList.get(x);
    }
}
