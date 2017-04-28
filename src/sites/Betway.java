package sites;

import Main.Game;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by navot on 4/24/2017.
 */
public class Betway extends Site {

    public Betway(String dateAndTime) {
        this.dateAndTime = dateAndTime;
        siteName = "Betway";
    }

    @Override
    protected void getRates() {

        doLeague("https://sports.betway.com/en/sports/grp/soccer/england/premier-league");
        doLeague("https://sports.betway.com/en/sports/grp/soccer/germany/bundesliga");
        doLeague("https://sports.betway.com/en/sports/grp/soccer/spain/la-liga");
        doLeague("https://sports.betway.com/en/sports/grp/soccer/italy/serie-a");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/france/ligue-1");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/netherlands/eerste-divisie");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/russia/premier-league");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/belgium/first-division-a");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/scotland/premiership");
//        doLeague("https://sports.betway.com/en/sports/grp/soccer/portugal/primeira-liga");

    }

    @Override
    protected void initSite() {
        driver = new ChromeDriver();
        driver.get("https://sports.betway.com/en/sports/grp/soccer/england/premier-league");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            driver.findElement(By.xpath("//div[@class='closeButton icon-cross']")).click();
        }catch (Exception e){
            System.out.println("no x at betway");
        }
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div[1]")).click();
        /*
         list = driver.findElements(By.className("dropdownSelectedOptionText"));
        for (WebElement ele : list) {
            if (ele.getText().equals("Fractional")){
                ele.click();
            }
        }*/
        List<WebElement> list =driver.findElements(By.className("itemLabel"));
        for (WebElement ele : list) {
            if (ele.getText().equals("Decimal")){
                ele.click();
            }
        }
    }

    @Override
    protected void doLeague(String leagueURL) {
        driver.get(leagueURL);

        String league = "";
        List<WebElement> list = null;
        league = leagueURL.substring(leagueURL.lastIndexOf("soccer/") + "soccer/".length());
        league = league.substring(0, league.lastIndexOf("/"));
        try {
            Thread.sleep(10000);
            list = driver.findElements(By.className("eventListItem"));
            buildGameList(leagueURL, league, list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void buildGameList(String leagueURL, String league, List<WebElement> list) {
        String siteName = "Betway";
        for (int i = 0; i < list.size(); i++) {
            List<WebElement> teamsList = list.get(i).findElements(By.className("teamNameEllipsisContainer"));
            String homeTeam = teamsList.get(0).getText();
            String awayTeam = teamsList.get(1).getText();
            List<WebElement> oddsList = list.get(i).findElements(By.className("odds"));
            double homeRate = 0;
            double drawRate = 0;
            double awayRate = 0;
            try {
                homeRate = Double.parseDouble(oddsList.get(0).getText());
                drawRate = Double.parseDouble(oddsList.get(1).getText());
                awayRate = Double.parseDouble(oddsList.get(2).getText());
                WebElement element = list.get(i).findElement(By.className("eventSummaryContainer"));
                String s = element.getAttribute("data-container");
                String date = s.substring(s.indexOf("2017-"), s.indexOf("2017-") + 10);
                Game game = new Game(siteName, league, getDateFromString(date), homeTeam, awayTeam, homeRate, drawRate, awayRate);
                rawGameList.add(game);
                System.out.println(game.toString());
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    protected Date getDateFromString(String date) {
        Date tempDate = new Date();
        SimpleDateFormat dateformat3 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tempDate = dateformat3.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempDate;
    }

    @Override
    public void run() {
        initSite();
        getRates();
        generateResultsFiles();
    }
}
