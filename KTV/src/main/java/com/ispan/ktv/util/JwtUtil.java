package com.ispan.ktv.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString; // 从配置文件读取密钥

    // 获取 SecretKey 实例
    private SecretKey getSecretKey() {
        // 确保密钥长度符合要求
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("密钥长度不足，请使用至少 32 字节的密钥");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成 JWT 令牌
    public String generateToken(String idNumber) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(idNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 天有效
                .signWith(getSecretKey(), SignatureAlgorithm.HS256); // 使用密钥进行签名

        return builder.compact();
    }

    // 从 JWT 令牌中获取主题
    public String getSubject(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()) // 使用密钥进行解析
                    .build();
            Claims claims = parser.parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // 处理解析错误，例如无效的令牌
            return null;
        }
    }
}
