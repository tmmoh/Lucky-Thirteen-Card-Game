package lucky.players;

import lucky.LuckyThirdteen;

public abstract class Player {
    public final LuckyThirdteen game;

    protected Player(LuckyThirdteen game) {
        this.game = game;
    }
}
