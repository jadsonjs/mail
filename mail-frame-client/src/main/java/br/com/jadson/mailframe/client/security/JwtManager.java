package br.com.jadson.mailframe.client.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtManager {

    @Value("${mail.jwt.secret}")
    private String secret;

    private int expirationTime = 5*60*1000; // 5 minutes

    public String generateKey() {
        return Jwts.builder()
                .setIssuer("mail-client")
                .setSubject("Mail Client Application")
                .setIssuedAt(new Date())
                .setNotBefore(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .setId(UUID.randomUUID().toString())
                .signWith(getJwtKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jws<Claims> jws = null;
            jws = Jwts.parserBuilder().setSigningKey(getJwtKey()).build().parseClaimsJws(token);
            if ((jws.getBody().getIssuer().equals("mail-client"))
                    && (jws.getBody().getExpiration().compareTo(new Date()) > 0)
                    && (jws.getBody().getNotBefore().compareTo(new Date()) < 0)
                    && ( UUID.fromString(jws.getBody().getId())) != null ) {
                return true;
            }
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
