package br.com.jadson.mailframe.client.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtManager {

    @Value("${mail.jwt.secret}")
    private String secret;

    private int expirationTime = 5*60*1000;

    public String generateKey() {
        return Jwts.builder()
                .setSubject("mail.client")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(getJwtKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getJwtKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JWT token: "+e.getMessage());
        }
        return false;
    }

    private SecretKey getJwtKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
