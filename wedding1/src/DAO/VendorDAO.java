package DAO;

import database.DBConnection;
import model.Vendor;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VendorDAO {

    DBConnection dbConn = DBConnection.getInstance();
    Connection conn = dbConn.createConnection();



    double[] p1C = {8000,8500,150,5000,6000};
    double[] p2C = {13000,10000,210,7500,13000};
    double[] p3C = {18000,15000,350,12000,15000};
    double[][] costs = {p1C,p2C,p3C};



    public boolean insertVendor(Vendor v) {
        try{
            PreparedStatement stmt = conn.prepareStatement("insert into users values(?,?,?,?,?,?)");
            stmt.setString(1,v.getId());
            stmt.setString(2,v.getName());
            stmt.setString(3, v.getEmail());
            String hashedPassword = BCrypt.hashpw(v.getPassword(), BCrypt.gensalt(12));
            stmt.setString(4,hashedPassword);
            stmt.setString(5,v.getMobile());
            stmt.setInt(6,1);
            int userRow = stmt.executeUpdate();
            if (userRow>0){
                System.out.println("insertion done into users");
            }else{
                System.out.println("insertion failed into users");
            }

            PreparedStatement stmt1 = conn.prepareStatement("insert into vendors values(?,?,?,?)");
            stmt1.setString(1,v.getId());
            stmt1.setString(2,v.getCompany());
            stmt1.setString(3, v.getAddress());
            stmt1.setDouble(4,0);

            int vendorRow = stmt1.executeUpdate();
            if (vendorRow>0){
                System.out.println("insertion done into vendors");
            }else{
                System.out.println("insertion failed into vendors");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }






    public void giveVendorAccess(String user_id) {
        int v_id = 0;

        LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> packageMap = new LinkedHashMap<>();

        try {

            Statement st = conn.createStatement();
            ResultSet vendor_id = st.executeQuery("select v_id from vendors where u_id = " + user_id);

            if (vendor_id.next()) {
                v_id = vendor_id.getInt(1);
            }

            Statement st1 = conn.createStatement();
            ResultSet distinctPID = st1.executeQuery("select distinct package_id from standard_package_service");
            ArrayList<Integer> pIDs = new ArrayList<>();

            while (distinctPID.next()) {
                pIDs.add(distinctPID.getInt(1));
                System.out.println(" ==========>    " + distinctPID.getInt(1));
            }

            for (int i = 1; i <= pIDs.size(); i++) {

                int p_id = i;

                LinkedHashMap<Integer, ArrayList<String>> pMap = new LinkedHashMap<>();

                Statement distinctSID = conn.createStatement();
                ResultSet rs1 = distinctSID.executeQuery("select distinct service_id from standard_package_service");
                //no of services
                while (rs1.next()) {

                    ArrayList<String> serviceOptions = new ArrayList<>();

                    int s_id = rs1.getInt(1);

                    int relation_id = 0;


                    PreparedStatement stmt1 = conn.prepareStatement("insert into package_service_vendor(p_id,s_id,s_price,v_id) values(?,?,?,?)");
                    stmt1.setInt(1, p_id);
                    stmt1.setInt(2, s_id);
                    stmt1.setDouble(3, costs[p_id - 1][s_id - 1]);
                    stmt1.setInt(4, v_id);
                    int rows = stmt1.executeUpdate();

                    if (rows > 0) {
                        System.out.println("inserted");
                    }

                    PreparedStatement stmt4 = conn.prepareStatement("select * from package_service_vendor where p_id = ? and s_id = ?");
                    stmt4.setInt(1, p_id);
                    stmt4.setInt(2, s_id);
                    ResultSet rs4 = stmt4.executeQuery();


                    if (rs4.next()) {
                        relation_id = rs4.getInt(1);
                        System.out.println("inserted into psv");
                    }

                    //options
                    PreparedStatement stmt5 = conn.prepareStatement("select option_name from standard_package_service where package_id = " + p_id + " and " + "service_id = " + s_id);
                    ResultSet options = stmt5.executeQuery();
                    while (options.next()) {

                        String option = options.getString(1);
                        serviceOptions.add(option);


                        pMap.put(s_id, serviceOptions);
                    }
                    packageMap.put(p_id, pMap);
                }

            }
        }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }














    ArrayList<Vendor> loadVendors(){

        ArrayList<Vendor> vendorArrayList = new ArrayList<>();
        try{
            Statement stmt1 = conn.createStatement();
            ResultSet users = stmt1.executeQuery("select * from users where r_id = 1");
            while(users.next()){
                String u_id = users.getString(1);
                String name = users.getString(2);
                String email = users.getString(3);
                String password = users.getString(4);
                String mob = users.getString(5);

                PreparedStatement stmt2 = conn.prepareStatement("select * from vendors where u_id = ?");
                stmt2.setString(1,u_id);

                ResultSet vendors = stmt2.executeQuery();

                while(vendors.next()){
                    Vendor v = new Vendor(vendors.getString(1),name,email,password,mob,vendors.getString(2),vendors.getString(3),vendors.getDouble(4));
                    giveVendorAccess(u_id);
                    vendorArrayList.add(v);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vendorArrayList;
    }

    public Vendor getVendorById(String id){
        Vendor v = null;
        try{

            PreparedStatement stmt2 = conn.prepareStatement("select * from vendors where v_id = ? ");
            stmt2.setString(1,id);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()){
                String company = rs2.getString(2);
                String address = rs2.getString(3);
                double rating = rs2.getDouble(4);

                PreparedStatement stmt = conn.prepareStatement("select * from users where u_id = ? ");
                stmt.setString(1,id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    String name = rs.getString(2);
                    String email = rs.getString(3);
                    String password = rs.getString(4);
                    String mobile = rs.getString(5);
                    v = new Vendor(id,name,email,password,mobile,company,address,rating);
                }
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return v;
    }




}



