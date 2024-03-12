package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.service.SocialLoginService;
import juwoncode.commonblogproject.util.LoggerProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/social")
public class SocialLoginViewController {
    private final Logger logger = LoggerProvider.getLogger(this.getClass());
    private final SocialLoginService socialLoginService;

    public SocialLoginViewController(SocialLoginService socialLoginService) {
        this.socialLoginService = socialLoginService;
    }

    @GetMapping("/login")
    public String getSocialLoginPage(@RequestParam(value = "type", required = false) String type) {
        if (StringUtils.isBlank(type)) {
            logger.info("An empty social login type parameter was found.");
            throw new NoSuchElementException();
        } else if (type.equals("kakao") || type.equals("naver")) {
            return socialLoginService.getAuthorizationUri(type);
        } else {
            logger.info("An unspecified social login type parameter was found. type={}", type);
            throw new IllegalArgumentException();
        }
    }

    @GetMapping("/login/process/{type}")
    public String processSocialLogin(@PathVariable(value = "type", required = false) String type, String code) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(code)) {
            logger.info("Social login type or code is empty. type={}, code={}", type, code);
            throw new NoSuchElementException();
        } else if (type.equals("kakao") || type.equals("naver")) {
            String accessToken = socialLoginService.getAccessToken(type, code);
        } else {
            logger.info("An unspecified social login type parameter was found. type={}", type);
            throw new IllegalArgumentException();
        }
    }
}
