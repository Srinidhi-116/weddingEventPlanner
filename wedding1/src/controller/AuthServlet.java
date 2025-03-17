package controller;

import org.json.JSONObject;
import service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("inside auth servlet get method");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        AuthService.AuthResult authResult = authService.checkAuthentication(request);

        System.out.println(authResult.isLoggedIn()+" "+authResult.isVendor()+" ");

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("loggedIn", authResult.isLoggedIn());
        jsonResponse.put("isVendor",authResult.isVendor());

        if (authResult.isLoggedIn()) {
            jsonResponse.put("email", authResult.getEmail());
            jsonResponse.put("userId", authResult.getId());
            jsonResponse.put("name", authResult.getName());
        }
        response.getWriter().write(jsonResponse.toString());
    }
}
