package lucky.players;

import lucky.LuckyThirdteen;

public class RandomPlayer extends ComputerPlayer {
    public RandomPlayer(LuckyThirdteen game, String moveString, String startingCards) {
        super(game, moveString, startingCards);
    }

    @Override
    protected void discardCard() {
        hand.discardRandomCard(game.discard);
    }
}
