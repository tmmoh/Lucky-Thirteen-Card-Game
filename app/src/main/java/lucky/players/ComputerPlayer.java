package lucky.players;

import ch.aplu.jgamegrid.GameGrid;
import lucky.LuckyThirdteen;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;


public abstract class ComputerPlayer extends Player {
    private final Iterator<String> moves;
    protected ComputerPlayer(LuckyThirdteen game, String moveString, String startingCards) {
        super(game, startingCards);

        // Parse moves
        if (moveString != null) {
            moves = Arrays.stream(moveString.split(",")).iterator();
        } else {
            moves = Collections.emptyIterator();
        }
    }

    @Override
    public void playRound() {
        if (moves.hasNext()) {
            // Automove
            autoPlayRound();
        } else {
            // Logic move
            logicPlayRound();
        }
    }

    protected void autoPlayRound() {
        String move = moves.next();
        String[] actions = move.split("-");
        String drawString = actions[0];
        game.pack.dealCardToHand(hand, drawString);

        GameGrid.delay(game.delayTime);

        if (actions.length > 1) {
            String discardString = actions[1];
            discardCard(discardString);
        } else {
            System.out.println("no discard auto move");
            System.out.println("discarding with player logic instead");
            GameGrid.delay(game.thinkingTime);
            discardCard();
        }
    }

    protected void logicPlayRound() {
        drawRandomCard();
        game.setStatusText("Player " + this + " thinking...");
        GameGrid.delay(game.thinkingTime);
        discardCard();
    }

}
