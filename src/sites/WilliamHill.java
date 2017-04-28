package sites;

import Main.Game;
import Main.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WilliamHill extends Site {

    public WilliamHill(String dateAndTime){
        this.dateAndTime = dateAndTime;
        siteName = "WilliamHill";
    }

    @Override
    protected void getRates() {

        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/295/English+Premier+League.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/315/German+Bundesliga.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/338/Spanish+La+Liga+Primera.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/321/Italian+Serie+A.html");
       /* doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/331/Portuguese+Primeira+Liga.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/312/French+Ligue+1.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/334/Russian+Premier+League.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/326/Turkish+Kupasi.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/297/Scottish+Premiership.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/28159/Belgian+1st+Division+A.html");
        doLeague("http://sports.williamhill.com/bet/en-gb/betting/t/1073/Dutch+Jupiler+League.html");*/
    }

    @Override
    protected void initSite() {
        driver = new ChromeDriver();
        driver.get("http://sports.williamhill.com/bet/en-gb/betting/t/295/English+Premier+League.html");
        driver.findElement(By.id("yesBtn")).click();
        Select dropdown = new Select(driver.findElement(By.id("oddsSelect")));
        dropdown.selectByIndex(1);
    }

    @Override
    protected void doLeague(String leagueURL) {
        driver.get(leagueURL);

        String league = "";
        List<WebElement> list = null;

        league = leagueURL.substring(leagueURL.lastIndexOf("/") + 1).substring(0, leagueURL.substring(leagueURL.lastIndexOf("/") + 1).indexOf("+"));
        list = driver.findElements(By.className("rowOdd"));
        buildGameList(leagueURL, league, list);
    }

    @Override
    protected void buildGameList(String leagueURL, String league, List<WebElement> list) {
        String siteName = "WilliamHill";
        for (WebElement element : list) {

            try {
                List<WebElement> inList = element.findElements(By.tagName("td"));
                String date = inList.get(0).getText();
                String teams = inList.get(2).findElement(By.tagName("a")).getText();
                String homeTeam = teams.substring(0, teams.indexOf(" v ")).trim();
                String awayTeam = teams.substring(teams.indexOf(" v ") + 3).trim();
                double homeRate = Double.parseDouble(inList.get(4).findElement(By.tagName("div")).getText());
                double drawRate = Double.parseDouble(inList.get(5).findElement(By.tagName("div")).getText());
                double awayRate = Double.parseDouble(inList.get(6).findElement(By.tagName("div")).getText());
                rawGameList.add(new Game(siteName, league, getDateFromString(date), homeTeam, awayTeam, homeRate, drawRate, awayRate));


            } catch (Exception e) {
                System.out.println("--------------------Skipped Big--------------------");
            }
        }
    }

    @Override
    protected Date getDateFromString(String date) {
        Date tempDate = new Date();
        if (!date.contains("Live")) {
            String[] dateArray = date.split(" ");
            int month = Utils.getIntFromMonth(dateArray[1]);
            String monthString = (month >= 10) ? (month + 1) + "" : "0" + (month + 1);
            String finalDateString = dateArray[0] + "/" + monthString + "/" + "2017";
            SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                tempDate = dateformat3.parse(finalDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
