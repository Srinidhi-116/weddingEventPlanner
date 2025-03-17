package DAO;

import database.DBConnection;
import model.Service;

import java.sql.*;

public class ServiceDAO {

    DBConnection dbConnection = DBConnection.getInstance();
    Connection conn = dbConnection.createConnection();

    public boolean insertService(Service s,String vendorId, int packageId){

        try{
            PreparedStatement stmt1 = conn.prepareStatement("insert into services(s_name,isScalable,unit_guest_count,hasOption) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1,s.getName());
            stmt1.setBoolean(2,s.getScalability());
            stmt1.setInt(3,s.getUnitGuestCount());
            stmt1.setBoolean(4,s.getOptionAvailability());
            int rows = stmt1.executeUpdate();

            int s_id = -1;
            if (rows > 0) {
                System.out.println("inserted into services");
                //get generated key (s_id)
                ResultSet generatedKeys = stmt1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    s_id = generatedKeys.getInt(1);
                }
                generatedKeys.close();
            } else {
                throw new RuntimeException("Failed to insert into services table");
            }


            PreparedStatement stmt2 = conn.prepareStatement("insert into package_service_vendor(s_price,p_id,s_id,v_id) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            stmt2.setDouble(1,s.getPrice());
            stmt2.setInt(2,packageId);
            stmt2.setInt(3,s_id);
            stmt2.setString(4,vendorId);

            int rows2 = stmt2.executeUpdate();
            int relation_id =-1;

            if(rows2>0){
                System.out.println("inserted into psv");
                ResultSet generatedKeys = stmt2.getGeneratedKeys();
                if (generatedKeys.next()) {
                    relation_id = generatedKeys.getInt(1);
                }
                generatedKeys.close();
            }else{
                throw new RuntimeException("Failed to insert into psv table");
            }

            if(s.getOptionAvailability()){
                for(String option : s.getOptions()){
                    PreparedStatement stmt3 = conn.prepareStatement("insert into options(o_name,relation_id) values(?,?)");
                    stmt3.setString(1,option);
                    stmt3.setInt(2,relation_id);
                    int rows3 = stmt3.executeUpdate();
                    if(rows3>0){
                        System.out.println("inserted into options");
                    }
                }
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Failed to insert the service");
        }
        return true;
    }

    public Service getServiceById(String id){
        Service s = null;
        return null;
    }


    public boolean deleteService(int id){
        try{
            PreparedStatement stmt = conn.prepareStatement("delete from services where s_id = ?");
            stmt.setInt(1,id);
            int row = stmt.executeUpdate();
            if(row >0){
                System.out.println("deleted from services");
            }else{
                System.out.println("deletion from services failed");
            }

            PreparedStatement stmt1 = conn.prepareStatement("select relation_id from package_service_vendor where s_id = ?");
            stmt1.setInt(1,id);
            ResultSet rs = stmt1.executeQuery();
            if(rs.next()){
                PreparedStatement stmt2 = conn.prepareStatement("delete from options where relation_id = ?");
                stmt2.setInt(1,rs.getInt(1));
                int row2 = stmt2.executeUpdate();
                if(row2>0){
                    System.out.println("options deleted from options table");
                }else{
                    System.out.println("options deletion from options table failed");
                }

                PreparedStatement stmt3 = conn.prepareStatement("delete from package_service_vendor where s_id = ?");
                stmt3.setInt(1,id);
                int row3 = stmt3.executeUpdate();
                if(row3>0){
                    System.out.println("service deleted from package_service_vendor");
                }else{
                    System.out.println("service deletion from package_service_vendor failed");
                }
            }
        }catch(SQLException e){

        }
        return false;
    }

    public void updateServicePrice(String v_id, int s_id, double newPrice){
        try{
            PreparedStatement stmt = conn.prepareStatement("update package_service_vendor set s_price = ? where v_id = ? and s_id = ?");
            stmt.setDouble(1,newPrice);
            stmt.setString(2,v_id);
            stmt.setInt(3,s_id);

            int rows = stmt.executeUpdate();
            if (rows>0){
                System.out.println("s_price updated");
            }else{
                System.out.println("s_price update failed");
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }
}
