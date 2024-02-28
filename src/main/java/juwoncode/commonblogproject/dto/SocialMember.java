package juwoncode.commonblogproject.dto;

import juwoncode.commonblogproject.vo.RoleType;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class SocialMember extends DefaultOAuth2User {
    private RoleType roleType;

    @Builder
    public SocialMember(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes
            , String nameAttributeKey, RoleType roleType) {
        super(authorities, attributes, nameAttributeKey);
        this.roleType = roleType;
    }
}
