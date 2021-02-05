import com.google.common.collect.HashBasedTable;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class DaysData implements Serializable {
    private Hashtable<String, Campus> campusTable = new Hashtable<>();
    private ArrayList<Campus> campuses;
    private Date date;

    private static final long serialVersionUID = 3738109042284609900L;


    public DaysData(Date date){
        this.date = date;
    }

    public DaysData(ArrayList<Campus> data, Date date){
        campuses = data;
        this.date = date;
    }

    public Hashtable<String, Campus> createTable(){
        if (campusTable.size() != campuses.size()){
            campusTable = new Hashtable<>();
            for (Campus c : campuses){
                campusTable.put(c.getName(), c);
            }
        }
        return campusTable;
    }

    public void addData(Campus campus){campuses.add(campus);}

    public File toFile(String fileName) throws FileNotFoundException {
        return toFile(new File(fileName));
    }

    public File toFile(File file) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(file);
        pw.println("Campus Name, Campus Type, OnCampus Cases, OFfCampus Cases, Staff Cases, Total Cases");
        long time0 = System.nanoTime();
        //System.out.println(time0);
        for (Campus e : campuses){
            pw.println(e.toString());
        }
        long time1 = System.nanoTime();
        //System.out.println(time1 + "\n" + (time1-time0));
        pw.close();
        return file;
    }

    public String toString(){
        String out = date.toString() + "\n";
        for (Campus c : campuses){
            out += c.toString() + "\n";
        }
        return out;
    }

    public ArrayList<Campus> getCampuses() {
        return campuses;
    }

    public Date getDate() {
        return date;
    }

    public Campus getCampus(String name){
        createTable();
        return campusTable.get(name);
    }

}
