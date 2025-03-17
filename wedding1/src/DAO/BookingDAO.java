package DAO;

import database.DBConnection;
import model.Booking;
import model.Customer;
import model.Vendor;

import java.sql.*;
import java.time.LocalDate;

public class BookingDAO {

    DBConnection dbConnection = DBConnection.getInstance();
    Connection conn = dbConnection.createConnection();

    public boolean insertBooking(Booking b){
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into bookings(b_date,e_date,e_address,guest_count,status,u_id,v_id,price) values (?,?,?,?,?,?,?,?)");
            stmt.setDate(1,b.getBookingDate());
            stmt.setDate(2,b.getEventDate());
            stmt.setString(3,b.getAddress());
            stmt.setInt(4,b.getGuestCount());
            stmt.setString(5,"PENDING");
            stmt.setString(6,b.getCustomer().getId());
            stmt.setString(6,b.getVendor().getId());
            stmt.setDouble(7,b.getPrice());
            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("booking inserted");
                return true;
            }
            else{
                System.out.println("booking insertion failed");
            }
        }catch(SQLException e){
            throw new RuntimeException("booking insertion failed");
        }
        return false;
    }




    public Booking getBookingById(int id){
        Booking b = null;
        try{
            Customer c = null;
            Vendor v = null;
            PreparedStatement stmt = conn.prepareStatement("select * from bookings where b_id = ? ");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String u_id = rs.getString(8);
                String v_id = rs.getString(9);
                CustomerDAO customerDAO = new CustomerDAO();
                VendorDAO vendorDAO = new VendorDAO();
                Customer customer = customerDAO.getCustomerById(u_id);
                Vendor vendor = vendorDAO.getVendorById(v_id);
                b = new Booking(vendor,customer,rs.getDate(4).toString(),rs.getDate(3).toString(),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getDouble(10),rs.getInt(1));
            }
        }catch(SQLException e){
            throw new RuntimeException("booking status update failed");
        }
        return b;
    }




    public boolean insertCustomerChoice(int optionId, String userId, int bookingId){
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into customer_choices values (?,?,?)");
            stmt.setInt(1,optionId);
            stmt.setString(2,userId);
            stmt.setInt(3,bookingId);
            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("customer choice inserted");
                return true;
            }
            else{
                System.out.println("customer choice insertion failed");
            }
        }catch(SQLException e){
            throw new RuntimeException("customer choice insertion failed");
        }
        return false;
    }



    public boolean updateBookingStatus(String status){
        try{
            PreparedStatement stmt = conn.prepareStatement("update bookings set status = ? ");
            stmt.setString(1,status);
            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("booking status updated");
                return true;
            }
            else{
                System.out.println("booking status update failed");
            }
        }catch(SQLException e){
            throw new RuntimeException("booking status update failed");
        }
        return false;
    }


    public boolean insertNewPaymentRecord(Booking b){
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into payments(amount,b_id) values (?,?)");
            stmt.setDouble(1,b.getPrice());
            stmt.setInt(2,b.getId());


            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("payment record inserted");
                return true;
            }
            else{
                System.out.println("payment record insertion failed");
            }
        }catch(SQLException e){
            throw new RuntimeException("payment record insertion failed");
        }
        return false;
    }

    public boolean addPayment(Booking b){
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into payments(p_date,p_status) values (?,?)");
            stmt.setDate(1,Date.valueOf(LocalDate.now()));
            stmt.setString(2,"PAID");

            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("payment record UPDATED");
                return true;
            }
            else{
                System.out.println("payment record UPDATION failed");
            }
        }catch(SQLException e){
            throw new RuntimeException("payment record UPDATION failed");
        }
        return false;
    }

}
