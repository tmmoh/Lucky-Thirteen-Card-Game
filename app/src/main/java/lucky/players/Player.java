package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import lucky.LuckyThirdteen;

import java.util.Arrays;
import java.util.Iterator;

public abstract class Player {
    public final LuckyThirdteen game;
    public final Hand hand;

    protected Player(LuckyThirdteen game, Card... startingCards) {
        this.game = game;
        this.hand = new Hand(LuckyThirdteen.DECK);

        if (startingCards.length > 2)  {
            throw new IllegalArgumentException();
        }
        Arrays.stream(startingCards).forEach((c) -> hand.insert(c, false));
        //while (hand.getNumberOfCards() < 2) {
         //   hand.insert(game.drawRandom(), false);
        //}
    }

    public void playRound() {

    }

    protected Card drawCard() {
        //game.drawRandom();
        return null;
    }

    protected void discard(Card card) {
        card.removeFromHand(true);
    }
}
