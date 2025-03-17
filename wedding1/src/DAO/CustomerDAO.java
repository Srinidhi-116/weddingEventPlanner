package DAO;

import database.DBConnection;
import model.Customer;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    DBConnection dbConnection = DBConnection.getInstance();
    Connection conn = dbConnection.createConnection();

    public boolean insertNewCustomer(Customer c){
        try{
            PreparedStatement stmt1 = conn.prepareStatement("insert into users values(?,?,?,?,?,2)");
            stmt1.setString(1,c.getId());
            stmt1.setString(2,c.getName());
            stmt1.setString(3,c.getEmail());
            String hashedPassword = BCrypt.hashpw(c.getPassword(), BCrypt.gensalt(12));
            stmt1.setString(4,hashedPassword);
            stmt1.setString(5,c.getMobile());
            int rows = stmt1.executeUpdate();
            if(rows>0){
                return true;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
        return false;
    }

    public Customer getCustomerById(String id){
        Customer c = null;
        try{
            PreparedStatement stmt1 = conn.prepareStatement("select * from users where u_id like ?");
            stmt1.setString(1,id);
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()){
                c = new Customer(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5));
            }
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
        return c;
    }

    public boolean deleteCustomer(String id){
        try{
            PreparedStatement stmt1 = conn.prepareStatement("delete from users where u_id like ?");
            stmt1.setString(1,id);
            int rows = stmt1.executeUpdate();
            if(rows>0){
                return true;
            }
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
        return false;
    }


    public ArrayList<Customer> loadCustomersByVendorId(String id){

        ArrayList<Customer> customerArrayList = new ArrayList<>();
        try{
            PreparedStatement stmt1 = conn.prepareStatement("select * from users where u_id in (select u_id from bookings where v_id = ?)");
            stmt1.setString(1,id);
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()){
                customerArrayList.add(new Customer(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5)));
            }
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
        return customerArrayList;
    }



}
