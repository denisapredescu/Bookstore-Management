package com.master.bookstore_management.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.util.Strings;
public class JwtUtil {
    private static final String SECRET_KEY = "SECRET_KEY_123";

    public static String generateToken(String username, String role) {
        System.out.println(role);
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Invalid token");
        }
    }

    public static void verifyAdmin(String token) {
        if (token == null || Strings.isBlank(token)){
            throw new RuntimeException("User not logged in");
        }

        DecodedJWT decodedJWT = decodeToken(token);
        if(!decodedJWT.getClaim("role").asString().equals("ADMIN"))
            throw new RuntimeException("User not ADMIN");
    }
}
