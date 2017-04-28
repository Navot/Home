package Main;

import Main.Game;
import Main.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by navot on 4/18/2017.
 */
public class Normalize {

    public static void normalizeData(String pathToDataDir) throws Exception {

        File[] fileList = new File(pathToDataDir).listFiles();
        Map<String, String> map = getMapFromDictionary("C:\\Users\\navot\\IdeaProjects\\Home\\Dictionary.txt");

        for (File file : fileList) {
            try {
                if (file.getAbsolutePath().contains("Betway")) {
                    List<Game> gameList = Utils.readGamesFromCSV(file.getAbsolutePath());
                    for (int i = 0; i < gameList.size(); i++) {
                        changeLeagueSpelling(gameList.get(i));
                        changeTeamsSpelling(gameList.get(i), map);
                    }
                    Utils.writeListToFile(pathToDataDir + "\\betway_normData.csv", gameList);
                }
            } catch (Exception e) {
                throw new Exception("No Betway file found");
            }
        }
    }

    private static Map<String, String> getMapFromDictionary(String pathToDictionary) throws IOException {
        Map<String, String> tempMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(pathToDictionary));
        try {

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                tempMap.put(line.substring(0, line.indexOf("=")), line.substring(line.indexOf("=") + 1));
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return tempMap;
    }

    private static void changeLeagueSpelling(Game game) {
        switch (game.league) {
            case "germany":
                game.league = "German";
                break;
            case "italy":
                game.league = "Italian";
                break;
            case "england":
                game.league = "English";
                break;
            case "spain":
                game.league = "Spanish";
                break;
        }
    }

    private static void changeTeamsSpelling(Game game, Map<String, String> map) throws IOException {

        if (inDictionary(map, game.homeTeam)) {
            game.homeTeam = map.get(game.homeTeam);
        }
        if (inDictionary(map, game.awayTeam)) {
            game.awayTeam = map.get(game.awayTeam);
        }

    }

    private static boolean inDictionary(Map<String, String> map, String team) throws IOException {
        try {
            String teamNewName = map.get(team);
            if (teamNewName != null) return true;
        } catch (Exception e) {

        }
        return false;
    }
}
