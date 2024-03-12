package juwoncode.commonblogproject.dto;

import juwoncode.commonblogproject.vo.RoleType;
import juwoncode.commonblogproject.vo.SocialType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class SocialMember extends DefaultOAuth2User {
    private SocialType socialType;

    @Builder
    public SocialMember(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes
            , String nameAttributeKey, SocialType socialType) {
        super(authorities, attributes, nameAttributeKey);
        this.socialType = socialType;
    }
}
