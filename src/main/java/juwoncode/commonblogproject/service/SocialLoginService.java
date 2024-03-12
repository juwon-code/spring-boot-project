package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.SocialAttributes;
import juwoncode.commonblogproject.dto.SocialMember;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.util.LoggerProvider;
import juwoncode.commonblogproject.vo.SocialType;
import org.slf4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class SocialLoginService extends DefaultOAuth2UserService {
    private final Logger logger = LoggerProvider.getLogger(this.getClass());
    private final MemberRepository memberRepository;

    public SocialLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String usernameAttribute = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println(registrationId + "\n" + usernameAttribute + "\n" + attributes);

        SocialAttributes socialAttributes = SocialAttributes.of(registrationId, usernameAttribute, attributes);
        Member member = getMember(socialAttributes, socialType);

        return SocialMember.builder()
                .authorities(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRole())))
                .attributes(attributes)
                .nameAttributeKey(usernameAttribute)
                .socialType(socialType)
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