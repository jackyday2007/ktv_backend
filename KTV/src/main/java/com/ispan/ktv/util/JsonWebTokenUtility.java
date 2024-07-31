package com.ispan.ktv.util;
//import java.util.Base64;
//import javax.crypto.SecretKey;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.Password;
//import jakarta.annotation.PostConstruct;
//
//@Component
//public class JsonWebTokenUtility {
//
//    @Value("${jwt.token.expire}")
//    private long expire;
//
//    private byte[] base64EncodedSecret;
//    private char[] charArraySecret;
//
//    @PostConstruct
//    public void init() {
//        String secret = "ABCDEFGHJKLM23456789npqrstuvwxyz";
//        base64EncodedSecret = Base64.getEncoder().encode(secret.getBytes());
//        charArraySecret = new String(base64EncodedSecret).toCharArray();
//    }
//
//    public String createEncryptedToken(String data, Long lifespan) {
//        java.util.Date now = new java.util.Date();
//        if (lifespan == null) {
//            lifespan = this.expire * 30 * 60 * 1000;
//        }
//        long end = System.currentTimeMillis() + lifespan;
//        java.util.Date expiredate = new java.util.Date(end);
//
//        Password password = Keys.password(charArraySecret);
//        JwtBuilder builder = Jwts.builder()
//                .subject(data)
//                .issuedAt(now)
//                .expiration(expiredate)
//                .encryptWith(password, Jwts.KEY.PBES2_HS512_A256KW, Jwts.ENC.A256GCM);
//
//        return builder.compact();
//    }
//
//    public String validateEncryptedToken(String token) {
//        Password password = Keys.password(charArraySecret);
//        JwtParser parser = Jwts.parser()
//                .decryptWith(password)
//                .build();
//        try {
//            Claims claims = parser.parseEncryptedClaims(token).getPayload();
//            return claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String createToken(String data, Long lifespan) {
//        java.util.Date now = new java.util.Date();
//        if (lifespan == null) {
//            lifespan = expire * 30 * 60 * 1000;
//        }
//        long end = System.currentTimeMillis() + lifespan;
//        java.util.Date expiredate = new java.util.Date(end);
//
//        SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);
//        JwtBuilder builder = Jwts.builder()
//                .subject(data)
//                .issuedAt(now)
//                .expiration(expiredate)
//                .signWith(secretKey);
//
//        return builder.compact();
//    }
//
//    public String validateToken(String token) {
//        SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);
//        JwtParser parser = Jwts.parser()
//                .verifyWith(secretKey)
//                .build();
//        try {
//            Claims claims = parser.parseSignedClaims(token).getPayload();
//            return claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
