import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.*;

public class Scraper {

//look back at selenium

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Date date = new Date(System.currentTimeMillis());
        WebDriver driver;
        // get driver and open browser
        System.setProperty("webdriver.chrome.driver","C:\\Users\\djcma\\Desktop\\IntelliJWorkspace\\libs\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(10000000, 0));
        //driver.manage().window().setPosition(new Point(0, 0));
        String baseUrl = "https://www.friscoisd.org/departments/covid-19/covid-19-dashboard";

        // launch Fire fox and direct it to the Base URL
        driver.get(baseUrl);


        WebElement element = driver.findElement(By.id("table-full"));
        element = element.findElement(By.className("tabulator-tableHolder")).findElement(By.className("tabulator-table"));
        ArrayList<WebElement> elementsOfTable = (ArrayList<WebElement>) element.findElements(By.className("tabulator-row"));
        ArrayList<ArrayList<WebElement>> cellsByRow = new ArrayList<>();

        for (WebElement webElement : elementsOfTable) {
            cellsByRow.add((ArrayList<WebElement>) webElement.findElements(By.className("tabulator-cell")));
        }




        //format:
        //CAMPUS, type, student oncampus, student offcampus, staff, total


        ArrayList<Campus> campuses = new ArrayList<>();
        for (ArrayList<WebElement> e : cellsByRow){
            campuses.add(new Campus(date, e.get(0).getText().substring(0, e.get(0).getText().lastIndexOf(" ")),
                    e.get(0).getText().substring(e.get(0).getText().lastIndexOf(" ")+1),
                    Integer.parseInt(e.get(1).getText()), Integer.parseInt(e.get(2).getText()),
                    Integer.parseInt(e.get(3).getText()), Integer.parseInt(e.get(4).getText())));
        }
        DaysData data = new DaysData(campuses, date);

        ArrayList<DaysData> days = new ArrayList<>();
        days.add(data);

        File file = new File("data.dat");

        if (file.exists()) {
            FileInputStream in = new FileInputStream(new File("data.dat"));
            ObjectInputStream obin = new ObjectInputStream(in);
            Object next;
            try {
                while (((next = obin.readObject()) instanceof DaysData)) {
                    days.add((DaysData) next);
                }
            } catch (EOFException e) {
                System.out.println(e);
            }

            obin.close();
            in.close();
        }

        FileOutputStream out = new FileOutputStream(new File("data.dat"), false);
        ObjectOutputStream obj = new ObjectOutputStream(out);
        for (DaysData d : days){
            obj.writeObject(d);
        }


        obj.writeObject("done");
        obj.close(); out.close();

        driver.close();

    }

}
