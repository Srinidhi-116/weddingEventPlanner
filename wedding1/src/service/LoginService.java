package service;

import DAO.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final UserDAO userDAO = new UserDAO();
    private final JWT jwtUtility = JWT.getInstance();

    public UserLoginResult authenticateUser(String email, String password) throws Exception {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            throw new Exception("Invalid email or password");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Invalid email or password");
        }

//        if (!userDAO.isEmailVerified(email)) {
//            throw new Exception("Please verify your email before logging in");
//        }

        String token = jwtUtility.generateToken(Long.parseLong(user.getId()));
        return new UserLoginResult(token, user);
    }

    public UserLoginResult authenticateUserAfterVerification(String email) throws Exception {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }

        String token = jwtUtility.generateToken(Long.parseLong(user.getId()));
        return new UserLoginResult(token, user);
    }
}
