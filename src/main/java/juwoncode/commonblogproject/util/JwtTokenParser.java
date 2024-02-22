package juwoncode.commonblogproject.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenParser {
    private final SecretKey secretKey;

    public JwtTokenParser(@Value("${jwt.plain.key}") String plainKey) {
        secretKey = JwtTokenProvider.encryptKey(plainKey);
    }



    public String parseUsername(String token) {
        return parse(token, "username");
    }

    public String parseRole(String token) {
        return parse(token, "role");
    }

    private String parse(String token, String claimName) {
        token = token.replace("Bearer ", "");

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimName, String.class);
    }

    public boolean validate(String token) {
        token = token.replace("Bearer ", "");
        Date nowDate = new Date(System.currentTimeMillis());

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(nowDate);
    }
}
