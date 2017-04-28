package Main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by navot on 3/18/2017.
 */
public class Game {
    public Date date;
    public String teams;
    public String homeTeam;
    public String awayTeam;
    public double homeRate;
    public double drawRate;
    public double awayRate;
    public String[] result = new String[2];
    public String league;
    public String site="";
    public int resultBool = -100;
    public List<Integer> bet = new ArrayList();

    public Game(String line) {
        String[] splitLine = line.split(",");
        this.site = splitLine[0];
        this.league = splitLine[1];
        this.date = Utils.getDateFromStringFromCSV(splitLine[2],"yyyy_MM_dd");
        this.homeTeam = splitLine[3];
        this.awayTeam = splitLine[4];
        this.homeRate = Double.parseDouble(splitLine[5]);
        this.drawRate = Double.parseDouble(splitLine[6]);
        this.awayRate = Double.parseDouble(splitLine[7]);
        try {
            this.resultBool = Integer.parseInt(splitLine[7]);
        } catch (Exception e) {
            // System.out.println("No result for this one");
        }
    }

    public Game(Game game) {
        this.date = game.date;
        this.teams = game.teams;
        this.homeTeam = game.homeTeam;
        this.awayTeam = game.awayTeam;
        this.homeRate = game.homeRate;
        this.drawRate = game.drawRate;
        this.awayRate = game.awayRate;
        this.league = game.league;
        this.site = game.site;
    }

    public Game(String site,String league, Date date, String homeTeam,String awayTeam, double homeRate, double drawRate, double awayRate) {
        this.site = site;
        this.league = league;
        this.date = date;

        this.homeRate = homeRate;
        this.drawRate = drawRate;
        this.awayRate = awayRate;

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        String dateToString = new SimpleDateFormat("yyyy_MM_dd").format(date);
        if(!site.equals("")){
            return String.format("%-15s%-15s%-15s%-20s%-6s%-20s%-5s%-5s%-5s%-5s", site,league, dateToString, homeTeam, "vs", awayTeam, homeRate, drawRate, awayRate, resultBool);
        }
        if (resultBool != -100 ) {
            return String.format("%-15s%-15s%-20s%-6s%-20s%-5s%-5s%-5s%-5s", league, dateToString, homeTeam, "vs", awayTeam, homeRate, drawRate, awayRate, resultBool);
        } else {
            return String.format("%-15s%-15s%-20s%-6s%-20s%-5s%-5s%-5s", league, dateToString, homeTeam, "vs", awayTeam, homeRate, drawRate, awayRate);
        }
    }

    public void addResult(WebDriver driver) {
        String result = null;
        List<WebElement> list = null;
        try {
            result = driver.findElement(By.className("_UMb")).getText();
            if (result.equals("")) throw new NoSuchElementException("NoElement");
            this.result[0] = result.substring(0, result.indexOf('-')).trim();
            this.result[1] = result.substring(result.indexOf('-') + 1, result.indexOf("\n")).trim();
        } catch (NoSuchElementException e) {
            System.out.println("Failed in first try -> Trying something else");
            list = driver.findElements(By.className("_xZc"));
            if (list.size() == 2) {
                this.result[0] = list.get(0).getText();
                this.result[1] = list.get(1).getText();
            } else {
                throw new NoSuchElementException("No Element Even In Second Try");
            }
        }

    }

    public String printResults() {

        return String.format("%-20s%-6s%-20s%-5s%-5s%-5s", homeTeam, "vs", awayTeam, result[0], '-', result[1]);
    }

    public String toCSV() {

        String dateToString = new SimpleDateFormat("yyyy_MM_dd").format(date);
        if (!site.equals("")){
            return site+","+league + "," + dateToString + "," + homeTeam + "," + awayTeam + "," + homeRate + "," + drawRate + "," + awayRate;
        }else return league + "," + dateToString + "," + homeTeam + "," + awayTeam + "," + homeRate + "," + drawRate + "," + awayRate;


    }

    public boolean equals(Game game) {
        if (this.date.compareTo(game.date) == 0 && this.homeTeam.equals(game.homeTeam)&& this.awayTeam.equals(game.awayTeam)) return true;
        return false;
    }

}
