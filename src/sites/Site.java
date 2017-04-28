package sites;

import Main.Game;
import Main.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class Site implements Runnable{
    protected List<Game> rawGameList = new ArrayList<>();
    protected WebDriver driver;
    protected String dateAndTime;
    protected String siteName;

    abstract protected Date getDateFromString(String date);

    abstract protected void initSite();

    abstract protected void doLeague(String league);

    protected void generateResultsFiles() {
        Utils.sortGameList(rawGameList);
        Utils.printList(siteName+" - sortedGameList - ", rawGameList);
        String timeLog = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        Utils.writeListToFile("fullData\\" + dateAndTime + "\\" + siteName + "_" + timeLog + ".csv", rawGameList);
        driver.quit();
    }

    abstract void buildGameList(String leagueURL, String league, List<WebElement> list);

    protected abstract void getRates();
}
