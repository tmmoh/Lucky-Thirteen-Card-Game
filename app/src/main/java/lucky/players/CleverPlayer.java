package lucky.players;

import ch.aplu.jcardgame.Card;
import lucky.LuckyThirdteen;

public class CleverPlayer extends ComputerPlayer {

    public CleverPlayer(LuckyThirdteen game, String moveString, String startingCards) {
        super(game, moveString, startingCards);
    }


    @Override
    protected void discardCard() {
        // Discard Cards = game.discard;
        // Shared Cards = game.playingArea;
        // Hand = hand;

        // Implemented discarding logic here
    }
}
