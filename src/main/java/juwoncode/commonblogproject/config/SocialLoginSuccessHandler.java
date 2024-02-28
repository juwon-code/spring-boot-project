package juwoncode.commonblogproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juwoncode.commonblogproject.dto.JwtTokenResponse;
import juwoncode.commonblogproject.dto.SocialMember;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.service.JwtTokenService;
import juwoncode.commonblogproject.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    public SocialLoginSuccessHandler(JwtTokenProvider jwtTokenProvider, JwtTokenService jwtTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        JwtTokenResponse token = jwtTokenProvider.createToken(authentication);
        jwtTokenService.saveRefreshToken(token.getRefreshToken());

        response.addHeader("Authorization", token.getGrantType() + " " + token.getAccessToken());
        response.addHeader("Refresh-Token", token.getGrantType() + " " + token.getRefreshToken());
        response.addHeader("Username", username);

        setResponseMessage(response, true, "Login was successful. Welcome " + username + "!!!");
    }

    private void setResponseMessage(HttpServletResponse response, boolean isOk, String message) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("ok", isOk);
        jsonObject.put("message", message);

        response.getWriter().print(jsonObject);
    }
}
