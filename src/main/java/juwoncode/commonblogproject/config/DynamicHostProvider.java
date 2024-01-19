package juwoncode.commonblogproject.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DynamicHostProvider {
    private HttpServletRequest request;

    public DynamicHostProvider(HttpServletRequest request) {
        this.request = request;
    }

    public String getHost() {
        if (request == null) {

        }

        String scheme = request.getScheme();
        String name = request.getServerName();
        int port = request.getServerPort();

        return String.format("%s://%s:%d/", scheme, name, port);
    }
}
