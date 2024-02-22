package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.dto.JwtTokenResponse;

public interface JwtTokenService {
    JwtTokenResponse updateAccessToken(String accessToken, String refreshToken);
    void saveRefreshToken(String refreshToken);
}
