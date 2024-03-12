package juwoncode.commonblogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SocialTokenRequest {
    private String clientId;
    private String grantType;
    private String redirectUri;
    private String tokenUri;
    private String code;
}
