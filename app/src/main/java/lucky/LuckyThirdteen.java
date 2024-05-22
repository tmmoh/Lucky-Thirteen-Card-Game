package lucky;// LuckyThirteen.java


import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import lucky.players.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class LuckyThirdteen extends CardGame {

    public static final int SEED = 30008;
    public static final Random RANDOM = new Random(SEED);
    private final Properties properties;
    private final StringBuilder logResult = new StringBuilder();
    private final List<List<String>> playerAutoMovements = new ArrayList<>();

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private final String version = "1.0";
    public static final int NB_PLAYERS = 4;
    public static final int NB_START_CARDS = 2;
    public static final int NB_FACE_UP_CARDS = 2;
    
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private static final int THIRTEEN_GOAL = 13;
    public static final Deck DECK = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // new Location(650, 575)
            new Location(575, 575)
    };
    private Actor[] scoreActors = {null, null, null, null};
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    public int thinkingTime = 2000;
    public int delayTime = 600;
    private Player[] players;
    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] scores = new int[NB_PLAYERS];

    private int[] autoIndexHands = new int [NB_PLAYERS];
    private boolean isAuto = false;
    private lucky.Hand playingArea;
    public lucky.Hand pack;

    Font bigFont = new Font("Arial", Font.BOLD, 36);

    private void initScore() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            // scores[i] = 0;
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }


    private int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }

    private int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }

    private int calculateMaxScoreForThirteenPlayer(int playerIndex) {
        List<Card> privateCards = players[playerIndex].hand.getCardList();
        List<Card> publicCards = playingArea.getCardList();
        Card privateCard1 = privateCards.get(0);
        Card privateCard2 = privateCards.get(1);
        Card publicCard1 = publicCards.get(0);
        Card publicCard2 = publicCards.get(1);

        int maxScore = 0;
        if (isThirteenCards(privateCard1, privateCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard1)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard2, publicCard1)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if(isThirteenCards(privateCard2, publicCard2)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        return maxScore;
    }

    private void calculateScoreEndOfRound() {
        List<Boolean> isThirteenChecks = Arrays.asList(false, false, false, false);
        for (int i = 0; i < players.length; i++) {
            isThirteenChecks.set(i, isThirteen(i));
        }
        List<Integer> indexesWithThirteen = new ArrayList<>();
        for (int i = 0; i < isThirteenChecks.size(); i++) {
            if (isThirteenChecks.get(i)) {
                indexesWithThirteen.add(i);
            }
        }
        long countTrue = indexesWithThirteen.size();
        Arrays.fill(scores, 0);
        if (countTrue == 1) {
            int winnerIndex = indexesWithThirteen.get(0);
            scores[winnerIndex] = 100;
        } else if (countTrue > 1) {
            for (Integer thirteenIndex : indexesWithThirteen) {
                scores[thirteenIndex] = calculateMaxScoreForThirteenPlayer(thirteenIndex);
            }

        } else {
            for (int i = 0; i < scores.length; i++) {
                scores[i] = getScorePrivateCard(players[i].hand.getCardList().get(0)) +
                        getScorePrivateCard(players[i].hand.getCardList().get(1));
            }
        }
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }

    private Card selected;


    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list) {
        int x = RANDOM.nextInt(list.size());
        return list.get(x);
    }


    private void dealingOut(Player[] players, int NB_PLAYERS, int nbCardsPerPlayer, int nbSharedCards) {
        ch.aplu.jcardgame.Hand hand = DECK.toHand(false);
        pack = new Hand(DECK);
        pack.insert(hand, true);

        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = pack.getCard(initialCard);
                if (card != null) {
                    card.removeFromHand(true);
                    playingArea.insert(card, true);
                }
            }
        }
        int cardsToShare = nbSharedCards - playingArea.getNumberOfCards();

        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty()) return;
            Card dealt = randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            playingArea.insert(dealt, true);
        }

    }


    private void addCardPlayedToLog(int player, List<Card> cards) {
        if (cards.size() < 2) {
            return;
        }
        logResult.append("P" + player + "-");

        for (int i = 0; i < cards.size(); i++) {
            Rank cardRank = (Rank) cards.get(i).getRank();
            Suit cardSuit = (Suit) cards.get(i).getSuit();
            logResult.append(cardRank.getRankCardLog() + cardSuit.getSuitShortHand());
            if (i < cards.size() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }

    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
    }

    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }


    private void playGame() {
        // End trump suit
        int winner = 0;
        int roundNumber = 1;
        for (int i = 0; i < NB_PLAYERS; i++) updateScore(i);

        List<Card>cardsPlayed = new ArrayList<>();
        addRoundInfoToLog(roundNumber);

        int nextPlayer = 0;
        while(roundNumber <= 4) {
            selected = null;
            boolean finishedAuto = false;

            Player player = players[nextPlayer];

            player.playRound();

            addCardPlayedToLog(nextPlayer, players[nextPlayer].hand.getCardList());
            if (selected != null) {
                cardsPlayed.add(selected);
                selected.setVerso(false);  // In case it is upside down
                delay(delayTime);
                // End Follow
            }

            nextPlayer = (nextPlayer + 1) % NB_PLAYERS;

            if (nextPlayer == 0) {
                roundNumber ++;
                addEndOfRoundToLog();

                if (roundNumber <= 4) {
                    addRoundInfoToLog(roundNumber);
                }
            }

            if (roundNumber > 4) {
                calculateScoreEndOfRound();
            }
            delay(delayTime);
        }
    }

    private void initGame() {
        players = new Player[NB_PLAYERS];

        playingArea = new Hand(DECK);
        dealingOut(players, NB_PLAYERS, NB_START_CARDS, NB_FACE_UP_CARDS);
        playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();

        for (int i = 0; i < NB_PLAYERS; i++) {
            String playerTypeKey = "players." + i;
            String playerType = properties.getProperty(playerTypeKey);
            String autoMovementKey = "players." + i + ".cardsPlayed";
            String autoMovement = properties.getProperty(autoMovementKey);
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCards = properties.getProperty(initialCardsKey);

            players[i] = switch (playerType.toLowerCase()) {
                case "human" -> new HumanPlayer(this, initialCards);
                case "random" -> new RandomPlayer(this, autoMovement, initialCards);
                case "basic" -> new BasicPlayer(this, autoMovement, initialCards);
                case "clever" -> new CleverPlayer(this, autoMovement, initialCards);
                default -> throw new IllegalStateException("Unexpected value: " + playerType.toLowerCase());
            };
        }

        for (int i = 0; i < NB_PLAYERS; i++) {
            players[i].hand.sort(Hand.SortType.SUITPRIORITY, false);
        }
        // graphics
        RowLayout[] layouts = new RowLayout[NB_PLAYERS];
        for (int i = 0; i < NB_PLAYERS; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            players[i].hand.setView(this, layouts[i]);
            players[i].hand.setTargetArea(new TargetArea(trickLocation));
            players[i].hand.draw();
        }
    }


    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScores();
        initScore();
        initGame();
        playGame();

        for (int i = 0; i < NB_PLAYERS; i++) updateScore(i);
        int maxScore = 0;
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] > maxScore) maxScore = scores[i];
        List<Integer> winners = new ArrayList<Integer>();
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
        addEndOfGameToLog(winners);

        return logResult.toString();
    }

    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
    }

    private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }

    private boolean isThirteenMixedCards(List<Card> privateCards, List<Card> publicCards) {
        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isThirteen(int playerIndex) {
        List<Card> privateCards = players[playerIndex].hand.getCardList();
        List<Card> publicCards = playingArea.getCardList();
        boolean isThirteenPrivate = isThirteenCards(privateCards.get(0), privateCards.get(1));
        boolean isThirteenMixed = isThirteenMixedCards(privateCards, publicCards);
        return isThirteenMixed || isThirteenPrivate;
    }
}
