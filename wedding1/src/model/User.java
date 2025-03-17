package model;

public class User {
    String id;
    String name;
    String email;
    String password;
    String mobile;
    int role_id;



    public User(String name, String email, String password, String mobile, int role_id)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role_id = role_id;
    }
    public User(String id, String name, String email, String password, String mobile, int role_id)
    {
        this(name, email, password, mobile, role_id);
        this.id = id;

    }

    public String getPassword(){
        return this.password;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }


    public String getEmail(){
        return this.email;
    }

    public int getRoleId(){
        return role_id;
    }

}
