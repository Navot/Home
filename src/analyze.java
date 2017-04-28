import Main.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by navot on 4/12/2017.
 */
public class analyze {

    public static void main(String[] args) throws IOException {

        String filePath = "C:\\Users\\navot\\IdeaProjects\\Home\\results\\08-09_04_2017.csv";
        ExecuteRound(filePath);

        filePath = "C:\\Users\\navot\\IdeaProjects\\Home\\results\\01_04_2017.csv";
        ExecuteRound(filePath);

        filePath = "C:\\Users\\navot\\IdeaProjects\\Home\\results\\4-5_04_2017.csv";
        ExecuteRound(filePath);
    }

    private static void ExecuteRound(String filePath) throws IOException {
        String roundFromFilePath = filePath.substring(1 + filePath.lastIndexOf("\\"), filePath.lastIndexOf("."));
        System.out.println("-------------------------------------- " + roundFromFilePath + " ------------------------------------------");

        List<Game> gameList = readGameWithResultsFromCSV(filePath);
        System.out.print(String.format("%-25s", "All(1-2.5):"));
        theGameSelectionStrategy(gameList);

        double avg_rate = getAvgBet(gameList);
        System.out.print(String.format("%-25s", "avg_bet(" + roundIt(avg_rate) + "-2.5):"));
        theGameSelectionStrategy(getGamesFromRange(gameList, avg_rate, 2.5));

        System.out.print(String.format("%-25s", "1.7-2.3:"));
        theGameSelectionStrategy(getGamesFromRange(gameList, 1.7, 2.3));


    }

    private static double roundIt(double doubleNum) {
        return Math.round(doubleNum * 100000.0) / 100000.0;
    }

    private static double getAvgBet(List<Game> gameList) {
        double sum_rate = 0;
        double avg_rate = 0;
        for (int i = 0; i < gameList.size(); i++) {
            sum_rate += gameList.get(i).homeRate;
        }
        avg_rate = sum_rate / gameList.size();
        return avg_rate;
    }

    private static List<Game> getGamesFromRange(List<Game> gameList, double from, double to) {

        return gameList.subList(index(gameList, from), index(gameList, to));
    }

    private static int index(List<Game> gameList, double bar_rate) {
        int i = 0;
        try {
            while (gameList.get(i).homeRate < bar_rate) {
                i++;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return i;
    }

    private static void theGameSelectionStrategy(List<Game> gameList) {

        double avg_bet;
        double avg_rate;
        double sum_rate = 0;

        avg_bet = 1000 / gameList.size();

        for (int i = 0; i < gameList.size(); i++) {
            sum_rate += gameList.get(i).homeRate;
        }
        avg_rate = sum_rate / gameList.size();
        for (int i = 0; i < gameList.size(); i++) {
            gameList.get(i).bet.add(0, (int) Math.ceil(avg_bet));
            gameList.get(i).bet.add(1, (int) Math.ceil((gameList.get(i).homeRate / avg_rate) * avg_bet));
            gameList.get(i).bet.add(2, (int) Math.ceil((avg_rate / gameList.get(i).homeRate) * avg_bet));
//            gameList.get(i).bet.add(4,(int) Math.ceil((Math.pow((avg_rate / gameList.get(i).homeRate),0.5) * avg_bet)));

        }

        doMath(gameList, 0, "equal");
        doMath(gameList, 1, "risky");
        doMath(gameList, 2, "solid");
        System.out.println();
    }

    private static void doMath(List<Game> gameList, int strategy, String strategyName) {
        int bet_sum = 0;
        double win_sum = 0;
        for (int i = 0; i < gameList.size(); i++) {
            Game game = gameList.get(i);
            bet_sum += game.bet.get(strategy);
            win_sum += (game.bet.get(strategy) * game.homeRate) * game.resultBool;
            double winPotential = roundIt(game.bet.get(strategy) * game.homeRate);
            double actual = roundIt(game.bet.get(strategy) * game.homeRate * game.resultBool);
            // System.out.println(game.toString() + "   " + game.bet.get(strategy) + "   " + winPotential + "  " + actual);
        }

        System.out.print(String.format("%-20s", strategyName + ": " + Math.round((((win_sum - bet_sum) / bet_sum) * 100) * 10000.0) / 10000.0 + "    "));

    }


    private static List<Game> readGameWithResultsFromCSV(String cvsFile) throws IOException {
        List<Game> tempGameList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(cvsFile));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                tempGameList.add(new Game(line));
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
        } finally {
            br.close();
        }
        return tempGameList;
    }
}
