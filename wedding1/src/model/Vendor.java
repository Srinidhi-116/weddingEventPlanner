package model;

public class Vendor extends User{

    private String id;
    String name;
    String email;
    private String password;
    String mobile;
    String company;
    String address;
    double rating;
//    String user_id;

    public Vendor(String id, String name, String email, String password, String mobile, String company, String address, double rating)
    {
//      this(name, email, password,mobile,company,address,rating);
        super(name, email, password,mobile,1);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.company = company;
        this.address = address;
        this.rating = rating;
    }




//    public Vendor(String name, String email, String password, String mobile, String company, String address, double rating)
//    {
//        super(name,email,password,mobile,2);
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.mobile = mobile;
//        this.company = company;
//        this.address = address;
//        this.rating = rating;
//    }


    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getMobile(){
        return mobile;
    }
    public String getCompany(){
        return company;
    }
    public String getAddress(){
        return address;
    }
    public double getRating(){
        return rating;
    }

//    public String getUserId(){
//        return user_id;
//    }
}
