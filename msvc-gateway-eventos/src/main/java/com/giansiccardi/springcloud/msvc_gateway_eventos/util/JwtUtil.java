package com.giansiccardi.springcloud.msvc_gateway_eventos.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;


@Component
public class JwtUtil {



    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public Claims validateToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();

    }
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
