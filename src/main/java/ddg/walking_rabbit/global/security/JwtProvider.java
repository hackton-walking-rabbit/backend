package ddg.walking_rabbit.global.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.access-secret}")
    private String jwtKeyString;

    private SecretKey jwtKey;

    @PostConstruct
    public void init() {
        if (jwtKeyString == null) {
            throw new IllegalArgumentException("jwtKeyString 값이 없습니다.");
        }
        this.jwtKey = Keys.hmacShaKeyFor(jwtKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username) {
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(jwtKey)
                .compact();
        return token;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith(jwtKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false; // 파싱/서명/만료/형식 오류 모두 false
        }
    }

    public String extractUsername(String token) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(jwtKey)
                .build()
                .parseClaimsJws(token);
        return claims.getBody().getSubject();
    }
}
