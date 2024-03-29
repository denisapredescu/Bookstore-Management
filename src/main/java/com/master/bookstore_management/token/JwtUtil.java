package com.master.bookstore_management.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.master.bookstore_management.exceptions.exceptions.InvalidTokenException;
import com.master.bookstore_management.exceptions.exceptions.UnauthorizedUserException;
import com.master.bookstore_management.exceptions.exceptions.UserNotLoggedInException;
import org.apache.logging.log4j.util.Strings;
public class JwtUtil {
    private static final String SECRET_KEY = "SECRET_KEY_123";

    public static String generateToken(String username, String role) {
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
            throw new InvalidTokenException("Invalid token");
        }
    }

    public static void verifyAdmin(String token) {
        String role = verifyIsLoggedIn(token);

        if(!role.equals("ADMIN"))
            throw new UnauthorizedUserException("User not ADMIN");
    }

    public static String verifyIsLoggedIn(String token) {
        if (token == null || Strings.isBlank(token)){
            throw new UserNotLoggedInException("User not logged in");
        }

        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("role").asString();
    }
}
