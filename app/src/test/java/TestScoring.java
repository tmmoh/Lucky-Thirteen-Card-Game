import lucky.LuckyThirdteen;
import lucky.PropertiesLoader;
import org.junit.Test;

import java.util.Properties;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * TestScoring class contains JUnit tests for the Lucky Thirteen game.
 * It includes tests to v alidate the scoring logic based on different game scenarios.
*/
public class TestScoring {
    /**
     * RoundData is an inner class that is used to store information about a specific round.
     * It includes the round number, player selections, and player scores.
     * 
     */
    class RoundData {
        int roundNumber;
        String playerSelections;
        int[] playerScores = new int[4];

        @Override
        public String toString() {
            return "Round " + roundNumber + ". Player Selections: "  + playerSelections + ". Scores: " +
                    playerScores[0] + "," + playerScores[1] + "," + playerScores[2] + "," + playerScores[3];
        }
    }

    /**
     * Converts a log line from the game into a RoundData object.
     * 
     * @param logLine       the log line to be parsed
     * @return              RoundData object containing information, or null if the log line is not relevant
     */
    private RoundData convertFromLogLine(String logLine) {
        if (!logLine.startsWith("Round")) {
            return null;
        }

        String[]scoreSplit = logLine.split("Score:");
        String roundMovementPart = scoreSplit[0];
        String scoreDataPart = scoreSplit[1];

        String[]roundSplit = roundMovementPart.split(":");
        String roundDataPart = roundSplit[0];
        String movementPart = roundSplit[1];
        movementPart = movementPart.substring(0, movementPart.length() - 1);
        int roundNumber = Integer.parseInt(roundDataPart.replaceAll("Round", ""));

        String[] playerScore = scoreDataPart.split(",");
        RoundData roundData = new RoundData();
        roundData.roundNumber = roundNumber;
        roundData.playerSelections = movementPart;
        for (int i = 0; i < roundData.playerScores.length; i++) {
            roundData.playerScores[i] = Integer.parseInt(playerScore[i]);
        }

        return roundData;
    }

    /**
     * Converts a log line from the game into an array of end game scores.
     * 
     * @param logLine       the log line to be parsed
     * @return              an array of end game scores, or null if the log line is not returned.
     */
    private int[] convertEndGameFromLogLine(String logLine) {
        if (!logLine.startsWith("EndGame:")) {
            return null;
        }

        String [] endGameScoreStrings = logLine.split("EndGame:");
        String endGameScoreString = endGameScoreStrings[1];
        String[] scoreStrings = endGameScoreString.split(",");
        int[] scores = new int[4];
        for (int i = 0; i < scores.length; i++) {
            scores[i] = Integer.parseInt(scoreStrings[i]);
        }

        return scores;
    }

    /**
     * Runs the game with the specified properties file and returns a Scanner for the game log output
     * 
     * @param propertiesFile    the properties file to configure the game
     * @return                  Scanner for reading the game log output
     */
    private Scanner runningGame(String propertiesFile) {
        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesFile);
        String logResult = new LuckyThirdteen(properties).runApp();
        Scanner scanner = new Scanner(logResult);
        assertTrue(scanner.hasNextLine());
        System.out.println("logResult = " + logResult);
        return scanner;
    }

    @Test(timeout = 60000)
    public void test1WinnerOriginal() {
        String testProperties = "properties/test1.properties";
        Scanner scanner = runningGame(testProperties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[]endGameScores = convertEndGameFromLogLine(line);

            if (endGameScores != null) {
                assertArrayEquals(new int[] { 100, 0, 0, 0 }, endGameScores);
            }
        }
    }

    /**
     * Test case to verify the scoring when there is one winner in the original game config.
     */
    @Test(timeout = 60000)
    public void test1WinnerExtension() {
        String testProperties = "properties/test2.properties";
        Scanner scanner = runningGame(testProperties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[]endGameScores = convertEndGameFromLogLine(line);

            if (endGameScores != null) {
                assertArrayEquals(new int[] { 100, 0, 0, 0 }, endGameScores);
            }
        }
    }

    /**
     * Test case to verify the scoring when there are multiple winners with scores in the original game config.
     */
    @Test(timeout = 60000)
    public void test2WinnerOriginal() {
        String testProperties = "properties/test3.properties";
        Scanner scanner = runningGame(testProperties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[]endGameScores = convertEndGameFromLogLine(line);

            if (endGameScores != null) {
                assertArrayEquals(new int[] { 30, 45, 0, 0 }, endGameScores);
            }
        }
    }

    /**
     * Test case to verify the scoring when there are multiple winners with scores in the extended game config.
     */
    @Test(timeout = 600000)
    public void test2WinnerExtension() {
        String testProperties = "properties/test4.properties";
        Scanner scanner = runningGame(testProperties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[]endGameScores = convertEndGameFromLogLine(line);

            if (endGameScores != null) {
                assertArrayEquals(endGameScores, new int[] { 26, 35, 0, 0 });
            }
        }
    }

    /**
     * Test case to verify the scoring when no player achieves a sum of thirteen
     */
    @Test(timeout = 600000)
    public void test0Winner() {
        String testProperties = "properties/test5.properties";
        Scanner scanner = runningGame(testProperties);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[]endGameScores = convertEndGameFromLogLine(line);

            if (endGameScores != null) {
                assertArrayEquals(new int[] { 38, 60, 17, 63 }, endGameScores);
            }
        }
    }
}
