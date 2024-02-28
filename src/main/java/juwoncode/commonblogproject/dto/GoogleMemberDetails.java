package juwoncode.commonblogproject.dto;

import java.util.Map;

public class GoogleMemberDetails extends SocialMemberDetails {
    public GoogleMemberDetails(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return getValue(attributes, "sub");
    }

    @Override
    public String getUsername() {
        return getValue(attributes, "name");
    }

    @Override
    public String getProfileUrl() {
        return getValue(attributes, "picture");
    }
}
