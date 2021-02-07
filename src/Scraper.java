import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Scraper {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean again = true;
        while (again) {
            Scanner input = new Scanner(System.in);
            String line;
            if (args.length != 0){
                line = args[0].replaceAll("-", "");
            }
            else {
                System.out.println("Enter action you want (Update/ReadOut/CSVCampus/AutoUpdate/stop/help): ");
                line = input.nextLine();
            }
            File file;
            switch(line){
                case "Update":
                    updateData();
                    break;
                case "ReadOut":
                    file = new File("data.dat");
                    if (!file.exists()){
                        System.out.println("data.dat file not found, enter .dat file location:");
                        file = new File(input.nextLine());
                    }
                    readOut(file);
                    break;
                case "CSVCampus":
                    file = new File("data.dat");
                    if (!file.exists()){
                        System.out.println("data.dat file not found, enter .dat file location:");
                        file = new File(input.nextLine());
                    }
                    csvCampus(file);
                    break;
                case "AutoUpdate":
                    automated();
                    break;
                case "stop":
                    again = false;
                    break;
                case "help":
                    System.out.println("Update: Check website and data.dat file\n" +
                            "ReadOut: Reads out data.dat file\n" +
                            "CSVCampus: Asks for a campus name and creates a csv of the data for that campus\n" +
                            "AutoUpdate: updates the data file every 24\n" +
                            "stop: stops the program");
                    break;
                default:
                    System.out.println("Command not understood,");
                    break;

            }
        }
    }

    private static void automated() {
        LocalDateTime time = LocalDateTime.now();
        Timer timer = new Timer ();
        TimerTask t = new TimerTask () {
            @Override
            public void run () {
                try {
                    updateData();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };
        long milToMidnight = (time.getHour() * 60 + time.getMinute())*1000*60;
        timer.schedule(t, milToMidnight, 86400000 );
    }

    public static void csvCampus(File file) throws IOException, ClassNotFoundException {
        ArrayList<DaysData> data = read(file);

        for (DaysData d : data){
            d.createTable();
        }

        ArrayList<Campus> campuses = data.get(0).getCampuses();
        ArrayList<String> names = new ArrayList<>();

        for (Campus c : campuses){
            names.add(c.getName());
        }

        Hashtable<String, ArrayList<Campus>> schoolsCon = new Hashtable<>();

        for (String s : names){
            ArrayList<Campus> campusesByName = new ArrayList<>();
            for (DaysData d : data){
                campusesByName.add(d.getCampus(s));
            }
            Collections.sort(campusesByName);
            schoolsCon.put(s, campusesByName);
        }

        Scanner in = new Scanner(System.in);

        boolean again = true;
        while (again) {
            System.out.println("Enter name of school for comparison:");
            String name = in.nextLine();
            try {
                ArrayList<Campus> campus = schoolsCon.get(name);

                PrintWriter pw = new PrintWriter(new File(name + ".csv"));
                pw.println("Campus Name, Campus Type, On Campus Cases, Off Campus Cases, Staff Cases, Total Cases, Date");
                for (Campus c : campus){
                    pw.println(c.toString());
                }
                pw.close();

                System.out.println(campus.toString());
                System.out.println("\nWould you like to compare another campus? (enter y or n)");
                again = in.nextLine().equals("y");
            } catch (NullPointerException e) {
                System.out.println("School does not exist");
            }
        }
    }

    public static ArrayList<DaysData> readOut(File file) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream obj = new ObjectInputStream(in);
        ArrayList<DaysData> data = new ArrayList<>();
        Object next;
        try{
            while (((next = obj.readObject()) instanceof DaysData)){
                data.add((DaysData) next);
            }} catch(EOFException e){
            System.out.println(e);
        }
        System.out.println("Number of data sets: " + data.size());
        for (DaysData d : data){
            System.out.println(d.toString());
        }
        return data;
    }

    public static ArrayList<DaysData> read(File file) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream(file);
        ObjectInputStream obj = new ObjectInputStream(in);
        ArrayList<DaysData> data = new ArrayList<>();
        Object next;
        try{
            while (((next = obj.readObject()) instanceof DaysData)){
                data.add((DaysData) next);
            }} catch(EOFException e){
        }
        return data;
    }

    public static void updateData() throws IOException, ClassNotFoundException {
        Date date = new Date(System.currentTimeMillis());
        WebDriver driver;
        //System.out.println(System.getProperty("os.name"));
        if ("Windows 10".equals(System.getProperty("os.name"))) {
            String file;
            if (new File("libs\\chromedriver.exe").exists()){
                file = "libs\\chromedriver.exe";
            } else if (new File("chromedriver.exe").exists()) {
                file = "chromedriver.exe";
            } else {
                Scanner input = new Scanner(System.in);
                System.out.println("chromedriver.exe not found, enter path to chromedriver.exe:");
                file = input.nextLine();
            }
            System.setProperty("webdriver.chrome.driver", file);
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            String file;
            if (new File("libs\\chromedriver").exists()){
                file = "libs\\chromedriver";
            } else if (new File("chromedriver").exists()) {
                file = "chromedriver";
            } else {
                Scanner input = new Scanner(System.in);
                System.out.println("chromedriver not found, enter path to chromedriver:");
                file = input.nextLine();
            }
            System.setProperty("webdriver.chrome.driver", file);
        }
        // get driver and open browser

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
                e.printStackTrace();
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
