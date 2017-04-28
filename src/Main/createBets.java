package Main;

import Main.Game;
import Main.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by navot on 4/13/2017.
 */
public class createBets {
    private static boolean toPrint = false;

    public static void main(String[] args) throws Exception {
        String roundDir = "C:\\Users\\navot\\IdeaProjects\\Home\\fullData\\04_28_133913";
       // Normalize.normalizeData(roundDir);
        List<Game> gameList = selectGameList(roundDir);
        generateBets(roundDir, gameList);

    }

    private static List<Game> selectGameList(String roundDir) throws IOException {
        List<Game> betwayGameList = Utils.readGamesFromCSV(roundDir + "\\betway_normData.csv");
        List<Game> williamGameList = new ArrayList<>();
        File[] fileList = new File(roundDir).listFiles();
        for (File file : fileList) {
            if (file.getName().startsWith("William")) {
                williamGameList = Utils.readGamesFromCSV(file.getAbsolutePath());
            }
        }
        List<Game> tempGameList = new ArrayList<>();

        for (Game betwayGame : betwayGameList) {
            boolean flag = false;
            for (int i = 0; i < williamGameList.size(); i++) {
                if (betwayGame.equals(williamGameList.get(i))) {
                    System.out.println(betwayGame.toString() + " - betway");
                    System.out.println(williamGameList.get(i).toString() + " - william");
                    if (betwayGame.homeRate >= 1.4) {
                        if (betwayGame.homeRate - 0.1 < betwayGame.awayRate) {
                            if (betwayGame.homeRate >= williamGameList.get(i).homeRate) {
                                System.out.println("adding betway");
                                Game gameToAdd = new Game(betwayGame);
                                gameToAdd.site = "Betway";
                                tempGameList.add(gameToAdd);

                            } else {
                                System.out.println("adding william");
                                Game gameToAdd = new Game(williamGameList.get(i));
                                gameToAdd.site = "WilliamHill";
                                tempGameList.add(gameToAdd);

                            }

                        } else {
                            System.out.println("Not Favorite Enough!");
                        }
                        flag = true;
                    } else {
                        System.out.println("Too low to play");
                    }
                }
            }
            if (!flag) {
                System.out.println(betwayGame.toString() + " - Not found in William");
            }
        }

        Utils.writeListToFile(roundDir + "\\bothSitesGames.csv", tempGameList);

        return tempGameList;
    }

    private static void generateBets(String roundDir, List<Game> gameList) throws IOException {

        List<Game> selectedGameList = new ArrayList<>(gameList);

        double avg_rate = getAvgRate(selectedGameList);

        updateGamesWithBets(selectedGameList, avg_rate);
        Utils.sortGameList(selectedGameList);
        writeCalcBetsToCSV(roundDir + "\\bets_"+roundDir.substring(roundDir.lastIndexOf("\\")+1)+".csv", selectedGameList);

    }

    private static void updateGamesWithBets(List<Game> gameList, double avg_rate) {
        double avg_bet = 1000 / gameList.size();
        for (int i = 0; i < gameList.size(); i++) {
            gameList.get(i).bet.add(0, (int) Math.ceil(avg_bet));
            gameList.get(i).bet.add(1, (int) Math.ceil((gameList.get(i).homeRate / avg_rate) * avg_bet));
            gameList.get(i).bet.add(2, (int) Math.ceil((avg_rate / gameList.get(i).homeRate) * avg_bet));

        }
    }

    private static void writeCalcBetsToCSV(String path, List<Game> gameList) {

        for (int i = 0; i < gameList.size(); i++) {
            System.out.println(gameList.get(i).toString() + "," + gameList.get(i).bet.get(0) + "," + gameList.get(i).bet.get(1) + "," + gameList.get(i).bet.get(2));
        }

        BufferedWriter writer = null;
        try {
            File logFile = new File(path);
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            for (int i = 0; i < gameList.size(); i++) {
                writer.write(gameList.get(i).toCSV() + "," + gameList.get(i).bet.get(0) + "," + gameList.get(i).bet.get(1) + "," + gameList.get(i).bet.get(2) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }


    }

    private static double getAvgRate(List<Game> gameList) {
        double sum_rate = 0;
        double avg_rate = 0;
        for (int i = 0; i < gameList.size(); i++) {
            sum_rate += gameList.get(i).homeRate;
        }
        avg_rate = sum_rate / gameList.size();
        return avg_rate;
    }


}
