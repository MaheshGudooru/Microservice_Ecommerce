package com.techouts.user_service.utils;

import com.techouts.user_service.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "bXktc3VwZXItc2VjcmV0LWtleS10aGF0LWlzLWF0LWxlYXN0LTMyLWJ5dGVzLWxvbmc=";

    SecretKey key = Keys.hmacShaKeyFor (Decoders.BASE64.decode (SECRET));

    public String generateToken(User user) {
        return Jwts.builder ()
                .setSubject (Integer.toString (user.getId ()))
                .claim ("X-User-ID", user.getId ())
                .claim ("X-Username", user.getName ())
                .setIssuedAt (new Date ())
                .setExpiration (new Date (System.currentTimeMillis () + 1000 * 60 * 10)) // 10 min expiration time
                .signWith (key, SignatureAlgorithm.HS256)
                .compact ();
    }

}
