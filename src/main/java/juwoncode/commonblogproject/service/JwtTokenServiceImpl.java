package juwoncode.commonblogproject.service;

import jakarta.transaction.Transactional;
import juwoncode.commonblogproject.domain.JwtToken;
import juwoncode.commonblogproject.dto.JwtTokenResponse;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.JwtTokenRepository;
import juwoncode.commonblogproject.util.JwtTokenParser;
import juwoncode.commonblogproject.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    public JwtTokenServiceImpl(JwtTokenParser jwtTokenParser, JwtTokenProvider jwtTokenProvider
            , JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public JwtTokenResponse updateAccessToken(String accessToken, String refreshToken) {
        if (!validateTokenWithDB(refreshToken)) {
            return null;
        }

        if (!validateTokenWithParser(refreshToken)) {
            expireTokenInDB(refreshToken);
            return null;
        }

        String username = jwtTokenParser.parseUsername(accessToken);
        String role = jwtTokenParser.parseRole(accessToken);
        Date nowDate = new Date(System.currentTimeMillis());

        accessToken = jwtTokenProvider.createAccessToken(username, role, nowDate);

        return JwtTokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        JwtToken jwtToken = JwtToken.builder()
                .refreshToken(refreshToken)
                .build();

        jwtTokenRepository.save(jwtToken);
    }

    private void expireTokenInDB(String token) {
        try {
            JwtToken jwtToken = jwtTokenRepository.findJwtTokenByRefreshToken(token)
                    .orElseThrow(NoSuchDataException::new);

            jwtToken.setExpired(true);

            jwtTokenRepository.save(jwtToken);
        } catch (NoSuchDataException e) {
            e.printStackTrace();
        }
    }

    private boolean validateTokenWithParser(String token) {
        return jwtTokenParser.validate(token);
    }

    private boolean validateTokenWithDB(String refreshToken) {
        return jwtTokenRepository.existsRefreshTokenByRefreshTokenAndExpired(refreshToken, false);
    }
}
