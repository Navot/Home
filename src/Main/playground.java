package Main;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class playground {

    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            System.out.println("Done Init Driver -> Strting Navigation");
            driver.get("https://www.bet365.com/");
            Utils.sleep(5000);
            driver.findElement(By.xpath("//div[contains(@class, 'wn-PreMatchItem') and contains(text(), 'Soccer')]"))
                    .click();
            Utils.sleep(500);
            driver.findElement(By.xpath(
                    "//span[contains(@class, 'sm-CouponLink_Title') and contains(text(), 'England Premier League')]"))
                    .click();
            Utils.sleep(500);
            System.out.println("Finished Navigation -> Start Collecting");

            List<WebElement> teamsAndTimes = driver.findElements(By.xpath(
                    "//div[contains(@class, 'ParticipantFixtureDetails-wide')]"));

            List<WebElement> oddsColumns = driver.findElements(By.xpath(
                    "//div[contains(@class, 'MarketOddsExpand')]"));

            for (int i = 0; i < teamsAndTimes.size(); i++) {
                WebElement match = teamsAndTimes.get(i);
                WebElement oddHome = oddsColumns.get(0).findElements(By.xpath(
                        ".//div[contains(@class, 'ParticipantOddsOnly80')]")).get(i);
                WebElement oddDraw = oddsColumns.get(1).findElements(By.xpath(
                        ".//div[contains(@class, 'ParticipantOddsOnly80')]")).get(i);
                WebElement oddAway = oddsColumns.get(2).findElements(By.xpath(
                        ".//div[contains(@class, 'ParticipantOddsOnly80')]")).get(i);

                WebElement time = match
                        .findElement(By.xpath(".//div[contains(@class,'rcl-ParticipantFixtureDetails_Details')]"));
                List<WebElement> teams = match.findElements(
                        By.xpath(".//div[contains(@class,'rcl-ParticipantFixtureDetailsTeam_TeamName')]"));

                WebElement date = match
                        .findElement(By.xpath("./preceding-sibling::div[contains(@class,'-isdate')][1]"));
                java.util.Date datetime = null;
                try {
                    datetime = new SimpleDateFormat("YYYY EEE dd MMM,hh:mm").parse("2022 " + date.getText() + "," + time.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.out.println(datetime.getTime() + "," + date.getText() + "," + time.getText() + ","
                        + teams.get(0).getText() + "," + teams.get(1).getText() + "," + oddHome.getText() + ","
                        + oddDraw.getText() + "," + oddAway.getText());

            }

        } finally {

            driver.quit();

        }
    }
}
