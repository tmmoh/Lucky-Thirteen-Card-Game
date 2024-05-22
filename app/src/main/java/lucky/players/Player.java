package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import lucky.Hand;
import lucky.LuckyThirdteen;

import java.util.Arrays;
import java.util.Iterator;

public abstract class Player {
    public final LuckyThirdteen game;
    public final Hand hand;

    protected Player(LuckyThirdteen game, String startingCards) {
        this.game = game;
        this.hand = new Hand(LuckyThirdteen.DECK);

        // Parse starting cards
        if (startingCards != null) {
            String[] cards = startingCards.split(",");
            for (String card: cards) {
                if (card.length() < 2) {
                    continue;
                }
                game.pack.dealCardToHand(hand, card);
            }
        }

        while (hand.getNumberOfCards() < 2) {
            game.pack.dealRandomCardToHand(hand);
        }
    }

    public abstract void playRound();

    protected void drawRandomCard() {
        game.pack.dealRandomCardToHand(hand);
    }

    protected void drawCard(String cardString) {
        game.pack.dealCardToHand(hand, cardString);
    }

    protected void discardCard(String cardString) {
        hand.discardCard(game.discard, cardString);
    }

    protected abstract void discardCard();
}
