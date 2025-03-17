package model;

import java.time.LocalDate;
import java.sql.Date;

public class Booking {

    int id;
    Vendor vendor;
    Customer customer;
    int choice_identifier;
    double price;
    Date b_date;
    Date e_date;
    String e_address;



    enum Status {
        PENDING,
        APPROVED,
        REJECTED,
        CLOSED;
    }
    int guestCount;
    Status stats;



    Booking(Vendor vendor, Customer customer, int choiceId,String e_date,String e_address,int guests)
    {
        this.vendor = vendor;
        this.customer = customer;
        this.stats = Status.PENDING;
        this.b_date = Date.valueOf(LocalDate.now());
        this.e_date = Date.valueOf(e_date);
        this.e_address = e_address;
//        this.choice_identifier = choiceId;
//        this.price = getPrice();
        guestCount = guests;
    }


    public Booking(Vendor vendor, Customer customer, String e_date, String b_date, String e_address, int guests, String stats, double price, int id)
    {
        this.vendor = vendor;
        this.customer = customer;
        this.b_date = Date.valueOf(b_date);
        this.e_date = Date.valueOf(e_date);
        this.e_address = e_address;
//        this.choice_identifier = choiceId;
        this.stats = Status.valueOf(stats);
        guestCount = guests;
        this.price = price;
        this.id = id;
    }

    public Date getBookingDate(){
        return b_date;
    }
    public Date getEventDate(){
        return e_date;
    }
    public Vendor getVendor(){
        return vendor;
    }
    public Customer getCustomer(){
        return customer;
    }
    public int getGuestCount(){
        return guestCount;
    }
    public double getPrice(){
        return price;
    }
    public String getAddress(){
        return e_address;
    }
    public int getId(){
        return id;
    }
}
