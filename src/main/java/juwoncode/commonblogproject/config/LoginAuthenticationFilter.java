package juwoncode.commonblogproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juwoncode.commonblogproject.dto.JwtTokenResponse;
import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.service.MemberDetailsService;
import juwoncode.commonblogproject.service.JwtTokenService;
import juwoncode.commonblogproject.util.JwtTokenProvider;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final MemberDetailsService memberDetailsService;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider
            , JwtTokenService jwtTokenService, MemberDetailsService memberDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenService = jwtTokenService;
        this.memberDetailsService = memberDetailsService;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/member/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberRequest.LoginDto loginDto = objectMapper.readValue(request.getInputStream()
                    , MemberRequest.LoginDto.class);

            String username = loginDto.getUsername();
            String password = loginDto.getPassword();
            Collection<? extends GrantedAuthority> roles = memberDetailsService.loadUserByUsername(username)
                    .getAuthorities();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, roles);

            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain
            , Authentication authResult) throws IOException, ServletException {
        setupResponse(response, HttpServletResponse.SC_OK);

        JwtTokenResponse token = jwtTokenProvider.createToken(authResult);
        jwtTokenService.saveRefreshToken(token.getRefreshToken());

        response.addHeader("Authorization", token.getGrantType() + " " + token.getAccessToken());
        response.addHeader("Refresh-Token", token.getGrantType() + " " + token.getRefreshToken());

        setResponseMessage(response, true, "Token issuance was successful.");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException failed) throws IOException, ServletException {
        setupResponse(response, HttpServletResponse.SC_BAD_REQUEST);

        setResponseMessage(response, false, "Token issuance failed.");

        String error;

        if (failed instanceof BadCredentialsException) {
            error = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (failed instanceof DisabledException) {
            error = "계정이 비활성화된 상태입니다. 인증메일을 확인해주세요.";
        } else {
            error = "알 수 없는 문제로 로그인에 실패했습니다. 관리자에게 문의하세요.";
        }

        response.sendRedirect("/member/login?message=" + URLEncoder.encode(error, StandardCharsets.UTF_8));
    }

    private void setupResponse(HttpServletResponse response, int status) {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
    }

    private void setResponseMessage(HttpServletResponse response, boolean isOk, String message) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("ok", isOk);
        jsonObject.put("message", message);

        response.getWriter().print(jsonObject);
    }
}
