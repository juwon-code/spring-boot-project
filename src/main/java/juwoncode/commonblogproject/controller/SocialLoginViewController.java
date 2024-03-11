package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.util.LoggerProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/social")
public class SocialLoginViewController {
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthURI;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectURI;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String naverAuthURI;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectURI;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    private final Logger logger = LoggerProvider.getLogger(this.getClass());

    @GetMapping("/login")
    public String getSocialLoginPage(@RequestParam(value = "type", required = false) String type) {
        if (StringUtils.isBlank(type)) {
            logger.info("An empty social login type parameter was found.");
            throw new NoSuchElementException();
        } else if (type.equals("kakao")) {
            return "redirect:" + kakaoAuthURI + "?response_type=code&client_id=" + kakaoClientId + "&redirect_uri="
                    + kakaoRedirectURI;
        } else if (type.equals("naver")) {
            return "redirect:" + naverAuthURI + "?response_type=code&client_id=" + naverClientId + "&redirect_uri="
                    + naverRedirectURI;
        } else {
            logger.info("An unspecified social login type parameter was found. type={}", type);
            throw new IllegalArgumentException();
        }
    }
}
