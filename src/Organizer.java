import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

public class Organizer {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        ArrayList<DaysData> data = Reader.read(new File("data.dat"));

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

                csv(campus, campus.get(0).getName() + ".csv");

                System.out.println(campus.toString());
                System.out.println("\nWould you like to compare another campus? (enter y or n)");
                again = in.nextLine().equals("y");
            } catch (NullPointerException e) {
                System.out.println("School does not exist");
            }
        }
    }

    private static void csv(ArrayList<Campus> campus, String name) throws IOException {
        PrintWriter pw = new PrintWriter(new File(name));
        for (Campus c : campus){
            pw.println(c.toString());
        }
        pw.close();
    }

}
