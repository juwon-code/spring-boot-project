package juwoncode.commonblogproject.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthExceptionHandler extends SimpleUrlAuthenticationFailureHandler {
    /**
     * 인증 실패 관련 이벤트를 처리하고, 예외에 따라 적절한 오류 메시지를 전달한다.
     * @param request
     *      인증 요청을 포함하는 {@link HttpServletRequest} 객체.
     * @param response
     *      인증 응답을 저장하는 {@link HttpServletResponse} 객체.
     * @param exception
     *      인증 실패에서 발생할 수 있는 예외.
     * @throws IOException
     *      입출력에서 발생할 수 있는 예외.
     * @throws ServletException
     *      서블릿에서 발생할 수 있는 예외.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
        String error;

        if (exception instanceof BadCredentialsException) {
            error = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            error = "계정이 비활성화된 상태입니다. 인증메일을 확인해주세요.";
        } else {
            error = "알 수 없는 문제로 로그인에 실패했습니다. 관리자에게 문의하세요.";
        }

        setDefaultFailureUrl("/member/login?message=" + URLEncoder.encode(error, StandardCharsets.UTF_8));
        super.onAuthenticationFailure(request, response, exception);
    }
}
