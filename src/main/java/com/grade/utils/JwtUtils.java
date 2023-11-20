package com.grade.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {


    private static final ThreadLocal<Claims> THREAD_LOCAL = new ThreadLocal<>();

    public static void setClaims(Claims claims) {
        THREAD_LOCAL.set(claims);
    }

    public static Claims getClaims() {
        return THREAD_LOCAL.get();
    }

    public static void removeClaims() {
        THREAD_LOCAL.remove();
    }
    public static String createToken(long ID,String username){
        Map<String,Object> claims=new HashMap<>();
        claims.put("username",username);
        claims.put("ID",ID);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,"Benson")
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();
        return token;
    }

    public static Claims parse(String token) {
        Claims claims;
        claims = Jwts.parser()
                    .setSigningKey("Benson")
                    .parseClaimsJws(token)
                    .getBody();
        //向threadlocal中存数据
        setClaims(claims);
        return claims;
    }
}
