package com.XiaoDai.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    public static String createToken(String username){
        Map<String,Object> claims=new HashMap<>();
        claims.put("username",username);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,"Benson")
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();
        return token;
    }

    public static Claims parse(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("Benson")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }
}
