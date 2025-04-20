package com.example.todo.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.todo.users.dto.UserInfoDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "c7826ff199c96ed2bfb405e47caf8e18fafa0f76840d41a087eb86265db5d0c9852f2c3ab7281b6924c6a0a973ca59eaa1fd820ac80f05a11de1be0c59e5518ae8549272d010629252b468c4def53c1f1b3ed398cee08c6787415f412a1e2db7e307b3f1e058c26d63fd7ae7a2dbd0c696fdb523975c0b5d64c500a057fdbf9cddb5a21a185532091814c1eaf704a5f73ffc61d4597c6c9220e5e30933a87c3d2c372a8718447ffd6ae60df61dc4b54736ecb0e323a08fd7cbe81b7c910585091852a0db18386f99465db5911f3a4cd0522e62b66c6391775c4b3f764f3d1dc8afeb1e561e86fa494ac6b2726a1a0877c15b0b1d57a7cfef18a1679b1c55ff9f";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateJwt(UserInfoDto userInfo) {
        return Jwts.builder()
                .subject(userInfo.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutes
                .signWith(getSignKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new JwtException("Invalid token format!" + e.getLocalizedMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims allClaims = extractAllClaims(token);
        return claimResolver.apply(allClaims);
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        System.out.println(extractClaim(token, Claims::getExpiration));
        System.out.println(new Date());
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserInfoDto user) {
        String signedEmail = extractUserEmail(token);
        String userEmail = user.getEmail();
        boolean hasTokenExpired = isTokenExpired(token);
        return (!hasTokenExpired && (signedEmail.equals(userEmail)));
    }
}
