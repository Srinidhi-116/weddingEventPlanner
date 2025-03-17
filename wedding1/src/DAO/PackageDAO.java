package DAO;

import database.DBConnection;
import model.Service;
import model.WeddingPackage;

import java.sql.*;
import java.util.ArrayList;

public class PackageDAO {

    DBConnection dbConnection = DBConnection.getInstance();
    Connection conn = dbConnection.createConnection();
    ServiceDAO serviceDAO = new ServiceDAO();

    public boolean insertNewPackage(WeddingPackage p,String vendorId){
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into packages(p_name) values (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,p.getName());
            int rows = stmt.executeUpdate();

            int p_id = -1;
            if (rows>0){
                System.out.println("inserted into packages");

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    p_id = generatedKeys.getInt(1);
                }
                generatedKeys.close();

            }else{
                System.out.println("insertion into packages failed");
            }

            for(Service service : p.getServices()){
                serviceDAO.insertService(service,vendorId,p_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert into packages");
        }
        return false;
    };





    public WeddingPackage getPackageById(int id){
        double totalPrice = 0;
        WeddingPackage weddingPackage = null;
        try{
            PreparedStatement stmt = conn.prepareStatement("select * from package_service_vendor where p_id = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Service> services = new ArrayList<>();
            while(rs.next()){
                double s_price = rs.getDouble("s_price");
                totalPrice+=s_price;
                int s_id = rs.getInt("s_id");
                int relation_id = rs.getInt("relation_id");
                ArrayList<String>options = new ArrayList<>();
                PreparedStatement stmt1 = conn.prepareStatement("select * from options where relation_id = ?");
                stmt1.setInt(1,relation_id);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()){
                    options.add(rs1.getString("o_name"));
                }
                PreparedStatement stmt2 = conn.prepareStatement("select * from services where s_id = ?");
                stmt2.setInt(1,s_id);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()){
                    services.add(new Service(rs2.getString("s_name"),s_price,options,rs2.getBoolean("isScalable"),rs2.getInt("unit_guest_count"),rs2.getBoolean("hasOption")));
                }

                PreparedStatement stmt3 = conn.prepareStatement("select p_name from packages where p_id = ?");
                stmt3.setInt(1,id);
                ResultSet rs3 = stmt3.executeQuery();
                if(rs3.next()){
                    weddingPackage = new WeddingPackage(rs3.getString("p_name"),services,totalPrice);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get package");
        }
        return weddingPackage;
    }




    public boolean deletePackage(String id){
        return false;
    }




    public ArrayList<WeddingPackage> getPackagesOfVendor(String vendorId){
        ArrayList<WeddingPackage> packages = new ArrayList<>();
        try{
            //get packages from packages table
            PreparedStatement stmt = conn.prepareStatement("select * from packages where p_id in (select p_id from package_service_vendor where v_id like ?)",Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,vendorId);
            ResultSet rs = stmt.executeQuery();


            while(rs.next()){
                String packageName = rs.getString("p_name");
                double totalPrice = 0;

                //get the relations from psv
                PreparedStatement stmt1 = conn.prepareStatement("select * from package_service_vendor where p_id = ?");
                stmt1.setInt(1,rs.getInt("p_id"));
                ResultSet rs1 = stmt1.executeQuery();
                ArrayList<Service> services = new ArrayList<>();

                while(rs1.next()){
                    ArrayList<String> options = new ArrayList<>();

                    //get options from options table using relation_id and add them to options array
                    PreparedStatement stmt2 = conn.prepareStatement("select * from options where relation_id = ?",Statement.RETURN_GENERATED_KEYS);
                    stmt2.setInt(1,rs1.getInt("relation_id"));
                    ResultSet rs2 = stmt2.executeQuery();

                    while(rs2.next()){
                        options.add(rs2.getString("o_name"));
                    }

                    //get details of service from services table
                    PreparedStatement stmt3 = conn.prepareStatement("select * from services where s_id = ?",Statement.RETURN_GENERATED_KEYS);
                    stmt3.setInt(1,rs1.getInt("s_id"));
                    ResultSet rs3 = stmt3.executeQuery();
                    Service service = null;

                    if(rs3.next()){
                        service = new Service(rs3.getString("s_name"),rs1.getDouble("s_price"),options,rs3.getBoolean("isScalable"),rs3.getInt("unit_guest_count"),rs3.getBoolean("hasOption"));
                        totalPrice += rs1.getDouble("s_price");
                    }
                    services.add(service);
                }

                WeddingPackage p = new WeddingPackage(packageName,services,totalPrice);
                packages.add(p);
            }
        }catch(SQLException e){
            throw new RuntimeException("");
        }
        return packages;
    }


}
