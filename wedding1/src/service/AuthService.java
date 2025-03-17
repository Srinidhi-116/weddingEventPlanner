package service;

import DAO.VendorDAO;
import model.Vendor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthService {

    public AuthResult checkAuthentication(HttpServletRequest request) {
        System.out.println("checking authentication");

        Cookie[] cookies = request.getCookies();


        if (cookies == null) {
            System.out.println("no cookies found");
            return new AuthResult(false,false, null, null, null);
        }

        System.out.println(cookies.length);

        for (Cookie cookie : cookies) {
            if ("auth_token".equals(cookie.getName())) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String email = (String) session.getAttribute("email");
                    String userId = (String) session.getAttribute("userId");
                    String name = (String) session.getAttribute("name");

                    VendorDAO vendorDAO =new VendorDAO();
                    Vendor v = vendorDAO.getVendorById(userId);
                    boolean isVendor = false;
                    if (v!=null){
                        isVendor = true;
                    }


                    if (email != null && userId != null) {
                        return new AuthResult(isVendor,true, email, userId, name);
                    }
                }
            }
        }
        return new AuthResult(false,false, null, null, null);
    }

    // Class to hold authentication result
    public static class AuthResult {
        private final boolean isVendor;
        private final boolean loggedIn;
        private final String email;
        private final String userId;
        private final String name;

        public AuthResult(boolean isVendor,boolean loggedIn, String email, String userId, String name) {
            this.isVendor = isVendor;
            this.loggedIn = loggedIn;
            this.email = email;
            this.userId = userId;
            this.name = name;
        }

        public boolean isVendor() {
            return isVendor;
        }

        public boolean isLoggedIn() {
            return loggedIn;
        }

        public String getEmail() {
            return email;
        }

        public String getId() {
            return userId;
        }

        public String getName() {
            return name;
        }
    }
}
