package juwoncode.commonblogproject.dto;

import java.util.Map;

public class KakaoMemberDetails extends SocialMemberDetails {
    private Map<String, Object> account;
    private Map<String, Object> profile;

    public KakaoMemberDetails(Map<String, Object> attributes) {
        super(attributes);
        account = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) account.get("profile");
    }

    @Override
    public String getId() {
        return getValue(attributes, "id");
    }

    @Override
    public String getUsername() {
        return getValue(profile, "nickname");
    }

    @Override
    public String getProfileUrl() {
        return getValue(profile, "thumbnail_image_url");
    }
}
