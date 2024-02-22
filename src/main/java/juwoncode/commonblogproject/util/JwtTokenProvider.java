package juwoncode.commonblogproject.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import juwoncode.commonblogproject.dto.JwtTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long ACCESS_VALID_TIME;
    private final long REFRESH_VALID_TIME;

    public JwtTokenProvider(@Value("${jwt.plain.key}") String plainKey) {
        secretKey = encryptKey(plainKey);
        ACCESS_VALID_TIME = 3600000L;
        REFRESH_VALID_TIME = 604800000L;
    }

    public JwtTokenResponse createToken(Authentication authentication) {
        String username = authentication.getName();
        Date nowDate = new Date(System.currentTimeMillis());
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createAccessToken(username, role, nowDate);
        String refreshToken = createRefreshToken(nowDate);

        return JwtTokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(String username, String role, Date nowDate) {
        Date expDate = new Date(nowDate.getTime() + ACCESS_VALID_TIME);

        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuedAt(nowDate)
                .expiration(expDate)
                .compact();
    }

    public String createRefreshToken(Date nowDate) {
        Date expDate = new Date(nowDate.getTime() + REFRESH_VALID_TIME);

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuedAt(nowDate)
                .expiration(expDate)
                .compact();
    }

    public static SecretKey encryptKey(String plainKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(plainKey));
    }
}
