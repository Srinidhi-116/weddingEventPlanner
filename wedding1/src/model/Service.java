package model;

import java.util.ArrayList;

public class Service {

    int id;
    String name;
    double price;
    boolean isAvailable;
    ArrayList<String> options = new ArrayList<>();
    boolean isScalable;
    int unitGuestCount;
    boolean hasOption;




    public Service(String name, double cost, ArrayList<String> options, boolean isScalable, int unitGuestCount, boolean hasOption) {
        this.name = name;
        this.price = cost;
        this.options = options;
        isAvailable = true;
        this.isScalable = isScalable;
        this.unitGuestCount = unitGuestCount;
        this.hasOption = hasOption;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }

    public ArrayList<String> getOptions(){
        return options;
    }
    public boolean getScalability(){
        return isScalable;
    }

    public int getUnitGuestCount(){
        return unitGuestCount;
    }

    public boolean getOptionAvailability(){
        return hasOption;
    }

}
