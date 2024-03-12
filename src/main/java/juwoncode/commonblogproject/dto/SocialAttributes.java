package juwoncode.commonblogproject.dto;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.vo.RoleType;
import juwoncode.commonblogproject.vo.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class SocialAttributes {
    private String attributeKey;
    private SocialMemberDetails socialMemberDetails;

    @Builder
    public SocialAttributes(String attributeKey, SocialMemberDetails socialMemberDetails) {
        this.attributeKey = attributeKey;
        this.socialMemberDetails = socialMemberDetails;
    }

    public static SocialAttributes of(String type, String attributeKey, Map<String, Object> attributes) {
        if (type.equals("naver")) {
            return ofNaver(attributeKey, attributes);
        }
        if (type.equals("kakao")) {
            return ofKakao(attributeKey, attributes);
        }

        return null;
    }

    private static SocialAttributes ofKakao(String attributeKey, Map<String, Object> attributes) {
        return SocialAttributes.builder()
                .attributeKey(attributeKey)
                .socialMemberDetails(new KakaoMemberDetails(attributes))
                .build();
    }

    private static SocialAttributes ofNaver(String attributeKey, Map<String, Object> attributes) {
        return SocialAttributes.builder()
                .attributeKey(attributeKey)
                .socialMemberDetails(new NaverMemberDetails(attributes))
                .build();
    }

    public Member toEntity(SocialType socialType, SocialMemberDetails socialMemberDetails) {
        return Member.builder()
                .socialId(socialMemberDetails.getId())
                .username(socialMemberDetails.getUsername())
                .profileUrl(socialMemberDetails.getProfileUrl())
                .socialType(socialType)
                .role(RoleType.USER)
                .build();
    }
}
