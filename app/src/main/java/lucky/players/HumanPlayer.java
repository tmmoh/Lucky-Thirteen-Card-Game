package lucky.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jgamegrid.GameGrid;
import lucky.LuckyThirdteen;

public class HumanPlayer extends Player {
    private Card selected;

    public HumanPlayer(LuckyThirdteen game, String startingCards) {
        super(game, startingCards);
        CardListener cardListener = new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }

    @Override
    public void playRound() {
        hand.setTouchEnabled(true);
        game.setStatus("Player 0 is playing. Please double click on a card to discard");
        selected = null;
        game.pack.dealRandomCardToHand(hand);
        while (selected == null) GameGrid.delay(game.delayTime);
        discardCard();
    }

    @Override
    protected void discardCard() {
        selected.removeFromHand(true);
        game.discard.insert(selected, true);
    }
}
