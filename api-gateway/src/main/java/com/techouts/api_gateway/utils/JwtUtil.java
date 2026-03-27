package com.techouts.api_gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "bXktc3VwZXItc2VjcmV0LWtleS10aGF0LWlzLWF0LWxlYXN0LTMyLWJ5dGVzLWxvbmc=";

    SecretKey key = Keys.hmacShaKeyFor (Decoders.BASE64.decode (SECRET));

    public boolean validateToken(String JWTtoken) {

        return !getClaimsFromToken (JWTtoken).getExpiration ().before (new Date ());
    }

    public String[] extractUserIdAndName(String JWTtoken) {

        String userId = getClaimsFromToken (JWTtoken).get ("X-User-ID", String.class);
        String userName = getClaimsFromToken (JWTtoken).get ("X-Username", String.class);

        return new String[]{userId, userName};
    }

    private Claims getClaimsFromToken(String JWTtoken) {

        return Jwts.parserBuilder ()
                .setSigningKey (key)
                .build ()
                .parseClaimsJws (JWTtoken)
                .getBody ();
    }

}
