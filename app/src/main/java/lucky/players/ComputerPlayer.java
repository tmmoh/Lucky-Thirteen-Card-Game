package lucky.players;

import lucky.LuckyThirdteen;

import java.util.Iterator;

public abstract class ComputerPlayer extends Player {
    private Iterator<String> moves;
    protected ComputerPlayer(LuckyThirdteen game) {
        super(game);
    }
}
