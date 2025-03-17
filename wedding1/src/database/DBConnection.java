package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    public static Connection conn = null;
    public static DBConnection dbConnection = null;


    private DBConnection(){

    }


    public static DBConnection getInstance(){
        if (dbConnection == null){
            return new DBConnection();
        }
        return dbConnection;
    }



    public Connection createConnection(){

        if(conn == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_2","root","new_password");
            }
            catch (SQLException e ) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if(conn != null){
            System.out.println("Connection established successfully");
        }
        else {
            System.out.println("Connection failed");
        }

        return conn;
    }


}
