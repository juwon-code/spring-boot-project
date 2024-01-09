package juwoncode.commonblogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RegisterDto {
        private String username;
        private String password;
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChangePasswordDto {
        private String username;
        private String oldPassword;
        private String newPassword;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class WithdrawDto {
        private String username;
        private String password;
    }
}
