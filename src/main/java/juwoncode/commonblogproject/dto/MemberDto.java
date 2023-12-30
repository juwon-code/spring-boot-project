package juwoncode.commonblogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.validation.annotation.Validated;

public class MemberDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class RequestDto {
        private String username;
        private String password;
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String username;
        private String password;
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginRequestDto {
        private String username;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChangePasswordRequestDto {
        private String username;
        private String originPassword;
        private String changePassword;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class WithdrawRequestDto {
        private String username;
        private String password;
    }
}
