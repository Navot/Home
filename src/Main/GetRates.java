package Main;

import Main.Utils;

import sites.Betway;
import sites.WilliamHill;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by navot on 3/18/2017.
 */
public class GetRates {

    static String dateAndTime = new SimpleDateFormat("MM_dd_HHmmss").format(Calendar.getInstance().getTime());
    static boolean screenShotsDirCreated;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\navot\\IdeaProjects\\Home\\chromedriver_win32\\chromedriver.exe");
        screenShotsDirCreated = false;  //makeScreenShotsDir(dateAndTime);
        Utils.makeDir("fullData\\" + dateAndTime);

        Thread betwayThread = new Thread(new Betway(dateAndTime));
        betwayThread .start();

        System.out.println("--------------------------------------------------------------------");

       Thread williamHillThread = new Thread(new WilliamHill(dateAndTime));
       williamHillThread.start();

        System.out.println("------------------------------Done Getting Raw Data----------------------------------");
    }


}
