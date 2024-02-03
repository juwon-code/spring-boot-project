package juwoncode.commonblogproject.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DynamicHostProvider {
    private final HttpServletRequest request;

    public DynamicHostProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 서버의 동적 호스트 주소를 반환한다.<br>
     * HttpServletRequest 객체로부터 얻은 데이터를 취합하여 "protocol://domain:port/" 형식의 호스트 주소를 반환한다.
     * @return 호스트 주소
     */
    public String getHost() {
        String scheme = request.getScheme();
        String name = request.getServerName();
        int port = request.getServerPort();

        return String.format("%s://%s:%d/", scheme, name, port);
    }
}
