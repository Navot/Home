package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by navot on 4/18/2017.
 */
public class Utils {

    public static List<Game> readGamesFromCSV(String cvsFile) throws IOException {
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

    public static void writeListToFile(String path, List<Game> gameList) {
        BufferedWriter writer = null;
        try {
            File logFile = new File(path);
            System.out.println(logFile.getCanonicalPath());
            writer = new BufferedWriter(new FileWriter(logFile));
            for (int i = 0; i < gameList.size(); i++) {
                writer.write(gameList.get(i).toCSV() + "\n");
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

    public static void printList(String listName, List<Game> gameListToSysPrint) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println(listName + " - ");
        for (int i = 0; i < gameListToSysPrint.size(); i++) {
            System.out.println(i + ".\t" + gameListToSysPrint.get(i).toString());
        }
        System.out.println("--------------------------------------------------------------------");
    }

    public static Date getDateFromStringFromCSV(String dateString, String pattern) {
        Date tempDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            tempDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempDate;
    }

    public static double roundIt(double doubleNum) {
        return Math.round(doubleNum * 100000.0) / 100000.0;
    }

    public static void takeScreenShot(String path) {

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = null;
        try {
            capture = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(capture, "bmp", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getIntFromMonth(String month) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMMMM").parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);

    }

    public static boolean makeDir(String dateAndTime) {
        File theDir = new File(dateAndTime);
        boolean result = false;
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                // handle it
                se.printStackTrace();
            }
            if (result) {
                System.out.println("DIR created - " + theDir.getAbsolutePath());
            }
        }
        return result;
    }

    public static boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<Game> sortGameList(List<Game> gameList) {
        List<Game> tempGameList = gameList;

        Collections.sort(tempGameList, new Comparator<Game>() {

            @Override
            public int compare(Game game1, Game game2) {
                if (game1.homeRate > game2.homeRate)
                    return 1;
                if (game1.homeRate < game2.homeRate)
                    return -1;
                else
                    return 0;
            }
        });

        return tempGameList;
    }
}
