package controller;

import model.User;
import org.json.JSONObject;
import service.LoginService;
import service.UserLoginResult;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final LoginService loginService = new LoginService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();

        try {
            JSONObject jsonRequest = readRequestBody(request);
            String email = jsonRequest.getString("email");
            String password = jsonRequest.getString("password");

            System.out.println("Received login request for email: " + email);

            // Authenticate user and get user details
            UserLoginResult loginResult = loginService.authenticateUser(email, password);
            String token = loginResult.getToken();
            User user = loginResult.getUser();


            // Set JWT Token in HttpOnly Cookie
            System.out.println("creating cookie");
            Cookie authCookie = new Cookie("auth_token", token);
//            authCookie.setHttpOnly(true);
//            authCookie.setSecure(true); // Ensure secure transmission (HTTPS required)
            authCookie.setPath("/");
            authCookie.setMaxAge(3 * 24 * 60 * 60); // 7 days expiration
            response.addCookie(authCookie);
            // Add CORS headers
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

// Set SameSite=None for cross-domain
            response.setHeader("Set-Cookie", "auth_token=" + token + "; HttpOnly; Secure; SameSite=None");

            // Store user session
            HttpSession session = request.getSession();
            session.setAttribute("email", user.getEmail());
            session.setAttribute("userId", user.getId());
            session.setAttribute("name", user.getName());

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Login successful");
            jsonResponse.put("user", new JSONObject()
                    .put("email", user.getEmail())
                    .put("userId", user.getId())
                    .put("name", user.getName())
                    .put("role_id",user.getRoleId())
            );
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        response.getWriter().write(jsonResponse.toString());
    }

    private JSONObject readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }
}
