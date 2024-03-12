package juwoncode.commonblogproject.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SocialLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
        setupResponse(response, HttpServletResponse.SC_BAD_REQUEST);

        String message;

        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "Invalid ID or password. please try again.";
        } else if (exception instanceof DisabledException) {
            message = "Your accound is deactivated. please check your verification email.";
        } else {
            message = "Login failed due to an unknown problem. please contact site administrator.";
        }

        setResponseMessage(response, false, message);
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
