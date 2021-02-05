import java.io.Serializable;
import java.util.Date;

public class Campus implements Serializable, Comparable<Campus> {
    private Date date;
    private String name;
    private String type;
    private int onCamp;
    private int offCamp;
    private int staff;
    private int total;

    private static final long serialVersionUID = 7769130633485785724L;


    public Campus(String toString){

    }

    public Campus(Date date, String name, String type, int onCamp, int offCamp, int staff, int total){
        this.name = name;
        this.type = type;
        this.onCamp = onCamp;
        this.offCamp = offCamp;
        this.staff = staff;
        this.total = total;
        this.date = date;
    }

    public String toString(){
        return name + ", " + type + ", " + onCamp + ", " + offCamp + ", " + staff + ", " + total + ", " + date;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getOnCamp() {
        return onCamp;
    }

    public int getOffCamp() {
        return offCamp;
    }

    public int getStaff() {
        return staff;
    }

    public int getTotal() {
        return total;
    }


    @Override
    public int compareTo(Campus o) {
        return this.date.compareTo(o.date);
    }
}
