package juwoncode.commonblogproject.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailConstants {
    public static final String VERIFICATION_MAIL_FROM = "juwoncode@gmail.com";
    public static final String VERIFICATION_MAIL_SUBJECT = "[common-blog-project] 블로그 프로젝트 회원가입을 환영합니다!!!";
    public static final String VERIFICATION_MAIL_TEXT = "<h1>블로그 프로젝트 회원가입 인증 메일 입니다.</h1><br>" +
            "<p>인증을 완료하기 위해 아래 링크를 클릭하세요.</p><br>" +
            "<a href='%s" + "email/verify/check?code=%s&type=%s' target='_blank'>인증 확인</a>";

    public static final String VERIFICATION_MAIL_EXPIRED_SUCCESS_LOG = "인증 메일 만료에 성공했습니다. 이메일: {}";
    public static final String VERIFICATION_MAIL_EXPIRED_FAILURE_LOG = "인증 메일 만료에 실패했습니다. 사유: 만료되지 않은 인증 메일이 존재하지 않습니다. 이메일: {}";
}
