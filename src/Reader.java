import java.io.*;
import java.util.ArrayList;

public class Reader {
    /*
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream(new File("data.dat"));
        ObjectInputStream obj = new ObjectInputStream(in);
        ArrayList<DaysData> data = new ArrayList<>();
        Object next;
        try{
        while (((next = obj.readObject()) instanceof DaysData)){
            data.add((DaysData) next);
        }} catch(EOFException e){
            System.out.println(e);
        }
        System.out.println(data.size());
        for (DaysData d : data){
            System.out.println(d.toString());
        }
    }
    */

    public static ArrayList<DaysData> read(File file) throws IOException, ClassNotFoundException {
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
}