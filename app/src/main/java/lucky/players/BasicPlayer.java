package lucky.players;

import ch.aplu.jcardgame.Card;
import lucky.LuckyThirdteen;
import lucky.Rank;
import lucky.Suit;

public class BasicPlayer extends ComputerPlayer {

    public BasicPlayer(LuckyThirdteen game, String moveString, String startingCards) {
        super(game, moveString, startingCards);
    }

    @Override
    protected void discardCard() {
        Card minCard = hand.getCardList().get(0);
        int min = ((Rank) minCard.getRank()).getScoreCardValue() * ((Suit) minCard.getSuit()).getMultiplicationFactor();
        for (Card card : hand.getCardList()) {
            int score = ((Rank) card.getRank()).getScoreCardValue() * ((Suit) card.getSuit()).getMultiplicationFactor();
            if (score < min) minCard = card;
        }

        minCard.removeFromHand(true);
        game.discard.insert(minCard, true);
    }
}
