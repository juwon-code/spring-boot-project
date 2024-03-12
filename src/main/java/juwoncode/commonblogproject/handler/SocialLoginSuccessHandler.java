package juwoncode.commonblogproject.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juwoncode.commonblogproject.dto.*;
import juwoncode.commonblogproject.service.JwtTokenService;
import juwoncode.commonblogproject.service.SocialLoginService;
import juwoncode.commonblogproject.util.JwtTokenProvider;
import juwoncode.commonblogproject.vo.SocialType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Component
public class SocialLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    public SocialLoginSuccessHandler(JwtTokenProvider jwtTokenProvider, JwtTokenService jwtTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SocialMember socialMember = (SocialMember) authentication.getPrincipal();
        SocialType socialType = socialMember.getSocialType();
        Map<String, Object> attributes = socialMember.getAttributes();
        SocialMemberDetails memberDetails;

        if (socialType.equals(SocialType.KAKAO)) {
            memberDetails = new KakaoMemberDetails(attributes);
        } else {
            memberDetails = new NaverMemberDetails(attributes);
        }

        JwtTokenRequest jwtTokenRequest = JwtTokenRequest.builder()
                .username(memberDetails.getUsername())
                .authorities(socialMember.getAuthorities())
                .build();

        JwtTokenResponse token = jwtTokenProvider.createToken(jwtTokenRequest);
        jwtTokenService.saveRefreshToken(token.getRefreshToken());

        response.sendRedirect("/member/login/process?access=" + token.getAccessToken() + "&refresh="
                + token.getRefreshToken() + "&username=" + URLEncoder.encode(memberDetails.getUsername(), StandardCharsets.UTF_8));
    }
}
