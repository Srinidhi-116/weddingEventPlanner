package model;

import java.util.ArrayList;

public class WeddingPackage {
    String name;
    ArrayList<Service> services;
    double totalPrice;


    public WeddingPackage(String name, ArrayList<Service> services,double totalPrice)
    {
        this.name = name;
        this.services = services;
        this.totalPrice = totalPrice;
    }




    double getTotalPrice()
    {
        double price = 0;
        for (Service service : services)
        {
            price += service.getPrice();
        }
        return price;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Service> getServices(){
        return services;
    }
}
