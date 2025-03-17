package service;


import DAO.UserDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JWT {

    // Secret Key for Signing JWT (Keep it Secure & Long Enough)
    private static final String SECRET = "6+yluJ8zWVxjPbLNFJ7VsaRPJpA70DmHYrYINZFiKWc=";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    private static JWT jwtUtil = null;

    private JWT() {
    }

    // Singleton Instance
    public static JWT getInstance() {
        if (jwtUtil == null) {
            jwtUtil = new JWT();
        }
        return jwtUtil;
    }

    // Generate Token (Using Unique User ID)
    public String generateToken(long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId)) // Storing unique user ID in token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000)) // 3 days expiry
                .signWith(SECRET_KEY)
                .compact();
    }

    // Validate & Extend Token if Near Expiry
    public String validateAndExtendToken(String token, HttpServletRequest req) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject(); // Extract user ID from token
            UserDAO userDAO = new UserDAO();

            if (userDAO.isUserExists(userId)) {
                String email = userDAO.getEmailById(userId); // Get email from DB
                req.setAttribute("email", email);

                // Check if token is close to expiry (less than 1 day remaining)
                Date expiration = claims.getExpiration();
                long remainingTime = expiration.getTime() - System.currentTimeMillis();
                if (remainingTime < 1L * 24 * 60 * 60 * 1000) {
                    return generateToken(Long.parseLong(userId)); // Renew the token
                }
                return token; // If token is still valid, return the same token
            } else {
                throw new RuntimeException("Token expired or user not found, please log in again.");
            }
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired, please log in again.");
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token.");
        }
    }
}

