package juwoncode.commonblogproject.dto;

import java.util.Map;

public abstract class SocialMemberDetails {
    protected Map<String, Object> attributes;

    public SocialMemberDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getUsername();

    public abstract String getProfileUrl();

    protected static String getValue(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        }

        return String.valueOf(map.get(key));
    }
}
