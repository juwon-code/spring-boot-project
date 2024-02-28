package juwoncode.commonblogproject.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao"), NAVER("naver"), GOOGLE("google");

    private String type;
}
