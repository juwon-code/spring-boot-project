package juwoncode.commonblogproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import juwoncode.commonblogproject.vo.MailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Valid
public class UserRequestDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "로그인 요청 DTO", description = "로그인에 필요한 데이터를 담고 있는 객체.")
    public static class Login {
        @NotNull(message = "아이디 파라미터가 비어있습니다.")
        @Schema(name = "아이디", description = "로그인할 회원의 아이디.")
        private String username;

        @NotNull(message = "비밀번호 파라미터가 비어있습니다.")
        @Schema(name = "비밀번호", description = "로그인할 회원의 비밀번호.")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name= "회원가입 요청 DTO", description = "회원가입에 필요한 데이터를 담고 있는 객체.")
    public static class Register {
        @NotNull(message = "아이디 파라미터가 비어있습니다.")
        @Pattern(regexp = "^[a-z]+[a-z0-9]{5,20}$", message = "아이디는 5 ~ 20자의 소문자여야 합니다.")
        @Schema(name = "아이디", description = "회원가입에 필요한 아이디.")
        private String username;

        @NotNull(message = "비밀번호 파라미터가 비어있습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[@$!%*#?&])[A-Za-z\\\\d@$!%*#?&]{8,15}$"
                , message = "비밀번호는 8 ~ 15자의 최소 하나의 영어 대소문자, 숫자, 특수문자를 포함하는 문자열이어야 합니다.")
        @Schema(name = "비밀번호", description = "회원가입에 필요한 비밀번호.")
        private String password;

        @NotNull(message = "별명 파라미터가 비어있습니다.")
        @Pattern(regexp = "^[가-힣A-Za-z0-9]{3,20}$", message = "별명은 3 ~ 20자의 한글, 영어, 숫자로만 이루어진 문자열이어야 합니다.")
        @Schema(name = "별명", description = "회원가입에 필요한 별명.")
        private String nickname;

        @NotNull(message = "이메일 파라미터가 비어있습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(name = "메일주소", description = "회원가입에 필요한 메일주소.")
        private String email;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name= "비밀번호 변경 요청 DTO", description = "비밀번호 변경에 필요한 데이터를 담고 있는 객체.")
    public static class ChangePassword {
        @NotNull(message = "아이디 파라미터가 비어있습니다.")
        @Schema(name = "아이디", description = "비밀번호를 변경할 회원의 아이디.")
        private String username;

        @NotNull(message = "기존 비밀번호 파라미터가 비어있습니다.")
        @Schema(name = "기존 비밀번호", description = "기존에 사용하고 있던 비밀번호.")
        private String oldPassword;

        @NotNull(message = "새로운 비밀번호 파라미터가 비어있습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[@$!%*#?&])[A-Za-z\\\\d@$!%*#?&]{8,15}$"
                , message = "비밀번호는 8 ~ 15자의 최소 하나의 영어 대소문자, 숫자, 특수문자를 포함하는 문자열이어야 합니다.")
        @Schema(name = "새로운 비밀번호", description = "새롭게 변경할 비밀번호.")
        private String newPassword;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "회원 활성화 요청 DTO", description = "회원 활성화에 필요한 데이터를 담고 있는 객체.")
    public static class Activate {
        @NotNull(message = "아이디 파라미터가 비어있습니다.")
        @Schema(name = "아이디", description = "활성화할 회원의 아이디.")
        private String username;

        @NotEmpty(message = "메일코드 파라미터가 비어있습니다.")
        @Schema(name = "메일코드", description = "인증메일의 고유 코드.")
        private String code;

        @NotEmpty(message = "메일타입 파라미터가 비어있습니다.")
        @Schema(name = "메일타입", description = "인증메일의 타입.")
        private MailType type;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name= "회원삭제 요청 DTO", description = "회원삭제에 필요한 데이터를 담고 있는 객체.")
    public static class Delete {
        @NotNull(message = "아이디 파라미터가 비어있습니다.")
        @Schema(name = "아이디", description = "삭제할 회원의 아이디.")
        private String username;

        @NotNull(message = "비밀번호 파라미터가 비어있습니다.")
        @Schema(name = "비밀번호", description = "삭제할 회원의 비밀번호.")
        private String password;
    }
}
