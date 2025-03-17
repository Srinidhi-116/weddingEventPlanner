package service;


import DAO.CustomerDAO;
import DAO.UserDAO;
import DAO.VendorDAO;
//import email.EmailUtil;
import model.Customer;
import model.Vendor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class SignupService {

    private UserDAO userDAO = new UserDAO();

    public boolean registerCustomer(String name, String email, String password,String mobile) throws Exception {
        // Validate input
        if (name == null || email == null || password == null || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        // Check if email already exists in DB
        if (userDAO.isEmailExists(email)) {
            throw new IllegalArgumentException("Email already registered.");
        }

        String userId = String.valueOf(System.nanoTime());

        CustomerDAO customerDAO = new CustomerDAO();
        boolean isInserted = customerDAO.insertNewCustomer(new Customer(userId,name,email,password,mobile));

        if (isInserted) {
            return true;
        } else {
            return false;  // Signup failed
        }
    }

    public boolean registerVendor(String name, String email, String password,String mobile,String company, String location, double rating) throws Exception {

        if (name == null || email == null || password == null || mobile == null ||
                company == null || location == null || name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || mobile.isEmpty() || company.isEmpty() || location.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        if (rating < 0) {
            throw new IllegalArgumentException("Rating cannot be negative.");
        }

        if (userDAO.isEmailExists(email)) {
            throw new IllegalArgumentException("Email already registered.");
        }

        String userId = String.valueOf(System.nanoTime());

        VendorDAO vendorDAO = new VendorDAO();
        boolean isInserted = vendorDAO.insertVendor(new Vendor(userId,name,email,password,mobile,company,location,0));

        if (isInserted) {
            return true;
        } else {
            return false;  // Signup failed
        }
    }


}

