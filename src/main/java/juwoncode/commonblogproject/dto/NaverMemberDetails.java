package juwoncode.commonblogproject.dto;

import java.util.Map;

public class NaverMemberDetails extends SocialMemberDetails {
    private Map<String, Object> response;

    public NaverMemberDetails(Map<String, Object> attributes) {
        super(attributes);
        response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return getValue(response, "id");
    }

    @Override
    public String getUsername() {
        return getValue(response, "nickname");
    }

    @Override
    public String getProfileUrl() {
        return getValue(response, "profile_image");
    }
}
