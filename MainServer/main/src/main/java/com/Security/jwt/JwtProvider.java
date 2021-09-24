package com.Security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
@Component
@Log
public class JwtProvider
{
    @Value("javamaster")
    private String jwtSecret;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateToken(String login)
    {
        log.severe("login"+login);
         String gen=Jwts.builder()
                .setSubject(login)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return gen;
    }

    public String getLoginFromToken(String token)
    {
        try
        {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        }catch (IllegalArgumentException e)
        {
            return  "invalid token";
        }

    }


}
