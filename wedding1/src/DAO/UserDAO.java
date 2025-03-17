package DAO;

import database.DBConnection;
import model.User;

import java.sql.*;
import java.time.Instant;

public class UserDAO {

    DBConnection dbConnection = DBConnection.getInstance();
    Connection conn = dbConnection.createConnection();



    public User getUserById(String id){
        return null;
    }

    public boolean deleteUser(String id){
        return false;
    }

    public boolean insertUser(String userId, String name, String email, String hashedPassword) {
        boolean isInserted = false;

        try {
            String sql = "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userId);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, hashedPassword); // ✅ Directly store hashed password

                int rowsAffected = ps.executeUpdate();
                isInserted = rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isInserted;
    }

    public boolean isEmailExists(String email) {
        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        exists = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }


    public boolean insertEmailVerification(String userId, String email, String token) {
        String query = "INSERT INTO email_verification (user_id, email, token, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // expiry time - 30min
            Timestamp expiryTime = Timestamp.from(Instant.now().plusSeconds(1800));
            stmt.setString(1, userId);
            stmt.setString(2, email);
            stmt.setString(3, token);
            stmt.setTimestamp(4, expiryTime);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmail(String email){
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User(rs.getString("u_id"),rs.getString("name"),email,rs.getString("password"),rs.getString("mobile"), rs.getInt("r_id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isUserExists(String userId){
        try {
            String sql = "SELECT * FROM users WHERE u_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getEmailById(String userId){
        try {
            String sql = "SELECT email FROM users WHERE u_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
