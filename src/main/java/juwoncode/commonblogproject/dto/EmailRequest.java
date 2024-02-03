package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import juwoncode.commonblogproject.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;

@Valid
public class EmailRequest {
    /**
     * 인증 메일 전송 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - email: 인증 메일 주소, 비워둘 수 없음, 이메일 패턴 적용.
     * - type: 인증 메일 타입, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     * @see
     *      Email
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SendDto {
        @NotEmpty(message = EMAIL_EMPTY_EXCEPTION)
        @Email(message = EMAIL_WRONG_EXCEPTION)
        private String email;

        @NotEmpty(message = EMAIL_TYPE_EMPTY_EXCEPTION)
        private String type;

        @NotNull(message = MEMBER_NULL_EXCEPTION)
        private Member member;
    }

    /**
     * 인증 메일 확인 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - code: 인증 메일 코드, 비워둘 수 없음.
     * - type: 인증 메일 타입, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CheckDto {
        @NotEmpty(message = EMAIL_CODE_EMPTY_EXCEPTION)
        private String code;

        @NotEmpty(message = EMAIL_TYPE_EMPTY_EXCEPTION)
        private String type;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ExpirationDto {
        @NotEmpty(message = EMAIL_CODE_EMPTY_EXCEPTION)
        private String code;

        @NotEmpty(message = EMAIL_TYPE_EMPTY_EXCEPTION)
        private String type;
    }
}
