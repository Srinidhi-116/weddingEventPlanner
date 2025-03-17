package controller;

import DAO.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Remove auth_token cookie
        Cookie authCookie = new Cookie("auth_token", "");
        authCookie.setPath("/");
        authCookie.setMaxAge(0); // Expire immediately
        response.addCookie(authCookie);

        response.setStatus(HttpServletResponse.SC_OK);

    }
}
