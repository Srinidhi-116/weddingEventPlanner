package controller;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import service.SignupService;

public class SignupServlet extends HttpServlet{


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("inside dopost of signup servlet");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject jsonResponse = new JSONObject();

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            JSONObject jsonReceived = new JSONObject(sb.toString());
            System.out.println(jsonReceived.toString());

            String name = jsonReceived.optString("name",null);
            System.out.println("-"+name);
            String email = jsonReceived.optString("email",null);
            System.out.println("-"+email);
            String password = jsonReceived.optString("password",null);
            System.out.println("-"+password);
            String mobile = jsonReceived.optString("mobile",null);
            System.out.println("-"+mobile);
            String role = jsonReceived.optString("role",null);
            System.out.println("-"+role);

            String location = (jsonReceived.has("location"))? jsonReceived.optString("location",null) : null;
            String company = (jsonReceived.has("company"))? jsonReceived.optString("company",null) : null;

            if(location !=null && company!=null){
                System.out.println("-"+location);
                System.out.println("-"+company);
            }

            SignupService signupService = new SignupService();
            boolean isSignupSuccessful = false;
            if(role.equalsIgnoreCase("customer")){
                System.out.println("customer role");
                isSignupSuccessful = signupService.registerCustomer(name,email,password,mobile);
            }else{
                System.out.println("vendor role");
                isSignupSuccessful = signupService.registerVendor(name,email,password,mobile,company,location,0);
            }

            System.out.println(isSignupSuccessful);

            if (isSignupSuccessful) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Signup successful! Check your email to verify your account.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Signup failed or email already registered.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

                response.getWriter().write(jsonResponse.toString());
                response.getWriter().flush();
                response.getWriter().close();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }
}
