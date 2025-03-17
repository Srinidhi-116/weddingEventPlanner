package model;

public class Customer extends User{

    private String id;
    String name;
    String email;
    private String password;
    String mobile;


    public Customer(String id, String name, String email, String password, String mobile)
    {
        super(id,name, email, password,mobile,2);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }

    public String getMobile(){
        return mobile;
    }
    public String getPassword(){
        return password;
    }


}
