import Main.Game;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by navot on 4/2/2017.
 */

public class GetResults {

    static Game first;
    static WebDriver driver;

    public static void main(String[] args) throws IOException {
        List<Game> gameList;
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\navot\\IdeaProjects\\Home\\chromedriver_win32\\chromedriver.exe");
        gameList = readGameCSV("C:\\Users\\navot\\IdeaProjects\\Home\\data\\20170407_131404.csv");
        driver = new ChromeDriver();
        for (Game game : gameList) {
            doGame(game);

        }
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < gameList.size(); i++) {
            System.out.println(i + ".\t" + gameList.get(i).printResults());
        }
        driver.quit();

    }


    private static List<Game> readGameCSV(String cvsFile) throws IOException {
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

    private static void doGame(Game game) {
        String prefix = "https://www.google.com/search?q=";
        String suffix = "&aqs=chrome..69i57j0l5.526j0j7&sourceid=chrome&ie=UTF-8";
        String mid = "&oq=";

        String homeTeam = game.homeTeam.replace(' ', '+').trim();
        String awayTeam = game.awayTeam.replace(' ', '+').trim();
        String url = prefix + homeTeam + "+vs+" + awayTeam + mid + homeTeam + "+vs+" + awayTeam + suffix;


        driver.get(url);
        try {
            game.addResult(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(driver.getPageSource());
//

    }
}
