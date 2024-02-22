package juwoncode.commonblogproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juwoncode.commonblogproject.dto.JwtTokenResponse;
import juwoncode.commonblogproject.dto.MemberDetails;
import juwoncode.commonblogproject.service.MemberDetailsService;
import juwoncode.commonblogproject.service.JwtTokenService;
import juwoncode.commonblogproject.util.JwtTokenParser;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class LoginAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenService jwtTokenService;
    private final MemberDetailsService memberDetailsService;

    public LoginAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenParser jwtTokenParser
            , JwtTokenService jwtTokenService, MemberDetailsService memberDetailsService) {
        super(authenticationManager);
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenService = jwtTokenService;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain) throws IOException, ServletException {
        setupResponse(response, HttpServletResponse.SC_OK);
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Refresh-Token");

        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        JwtTokenResponse jwtTokenResponse = jwtTokenService.updateAccessToken(accessToken, refreshToken);
        setResponseMessage(response, false, "Cannot found any access token.");

        if (jwtTokenResponse != null) {
            accessToken = jwtTokenResponse.getAccessToken();
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken));
            setResponseMessage(response, true, "Access token reissuance was successful.");
        } else {
            setResponseMessage(response, false, "Access token reissuance failed.");
        }

        response.setHeader("Authorization", accessToken);
        chain.doFilter(request, response);

        super.doFilterInternal(request, response, chain);
    }



    private Authentication getAuthentication(String token) {
        String username = jwtTokenParser.parseUsername(token);
        MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(memberDetails.getUsername(), memberDetails.getPassword()
                , memberDetails.getAuthorities());
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
