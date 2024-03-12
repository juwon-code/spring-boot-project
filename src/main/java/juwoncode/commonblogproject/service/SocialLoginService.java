package juwoncode.commonblogproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonIOException;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.SocialAttributes;
import juwoncode.commonblogproject.dto.SocialMember;
import juwoncode.commonblogproject.dto.SocialTokenRequest;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.util.LoggerProvider;
import juwoncode.commonblogproject.vo.SocialType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class SocialLoginService extends DefaultOAuth2UserService {
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoGrantType;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String naverAuthUri;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naverTokenUri;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String naverGrantType;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    private final Logger logger = LoggerProvider.getLogger(this.getClass());
    private final MemberRepository memberRepository;

    public SocialLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String getAuthorizationUri(String type) {
        if (type.equals("kakao")) {
            return createAuthorizationUri(kakaoAuthUri, kakaoRedirectUri, kakaoClientId);
        }

        return createAuthorizationUri(naverAuthUri, naverRedirectUri, naverClientId);
    }

    private String createAuthorizationUri(String authUri, String redirectUri, String clientId) {
        StringBuilder sb = new StringBuilder();

        sb.append("redirect:");
        sb.append(authUri);
        sb.append("?response_type=code&client_id=");
        sb.append(clientId);
        sb.append("&redirect_uri=");
        sb.append(redirectUri);

        return sb.toString();
    }

    public String getAccessToken(String type, String code) {
        SocialTokenRequest socialTokenRequest;

        if (type.equals("kakao")) {
            socialTokenRequest = SocialTokenRequest.builder()
                    .clientId(kakaoClientId)
                    .tokenUri(kakaoGrantType)
                    .redirectUri(kakaoRedirectUri)
                    .tokenUri(kakaoTokenUri)
                    .code(code)
                    .build();
        } else {
            socialTokenRequest = SocialTokenRequest.builder()
                    .clientId(naverClientId)
                    .tokenUri(naverGrantType)
                    .redirectUri(naverRedirectUri)
                    .tokenUri(naverTokenUri)
                    .code(code)
                    .build();
        }

        try {
            return receiveAccessToken(socialTokenRequest);
        } catch (JsonProcessingException e) {
            logger.info("Failed to deserialize JSON from the server.");
            return null;
        }
    }

    private String receiveAccessToken(SocialTokenRequest socialTokenRequest) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("grant_type", socialTokenRequest.getGrantType());
        httpBody.add("client_id", socialTokenRequest.getClientId());
        httpBody.add("redirect_uri", socialTokenRequest.getRedirectUri());
        httpBody.add("code", socialTokenRequest.getCode());

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(httpBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(socialTokenRequest.getTokenUri(), HttpMethod.POST
                , tokenRequest, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String usernameAttribute = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        SocialAttributes socialAttributes = SocialAttributes.of(registrationId, usernameAttribute, attributes);
        Member member = getMember(socialAttributes, socialType);

        return SocialMember.builder()
                .authorities(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRole())))
                .attributes(attributes)
                .nameAttributeKey(usernameAttribute)
                .roleType(member.getRole())
                .build();
    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals("naver")) {
            return SocialType.NAVER;
        }
        if (registrationId.equals("kakao")) {
            return SocialType.KAKAO;
        }

        return null;
    }

    private Member getMember(SocialAttributes socialAttributes, SocialType socialType) {
        try {
            String id = socialAttributes.getSocialMemberDetails().getId();

            return memberRepository.findMemberBySocialIdAndSocialType(id, socialType)
                    .orElseThrow(NoSuchDataException::new);
        } catch (NoSuchDataException e) {
            return saveMember(socialAttributes, socialType);
        }
    }

    private Member saveMember(SocialAttributes socialAttributes, SocialType socialType) {
        Member member = socialAttributes.toEntity(socialType, socialAttributes.getSocialMemberDetails());
        return memberRepository.save(member);
    }
}
