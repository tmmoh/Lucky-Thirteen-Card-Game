package lucky.players;

import ch.aplu.jcardgame.Card;
import lucky.LuckyThirdteen;

public class RandomPlayer extends ComputerPlayer {
    public RandomPlayer(LuckyThirdteen game, String moveString, String startingCards) {
        super(game, moveString, startingCards);
    }

    @Override
    protected void logicPlayRound() {

    }

    @Override
    protected void discardCard() {

    }
}
