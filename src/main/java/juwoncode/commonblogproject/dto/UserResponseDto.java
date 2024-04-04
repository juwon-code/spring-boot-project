package juwoncode.commonblogproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {
    @Getter
    @Builder
    @Schema(name = "회원가입 응답 DTO", description = "회원가입 요청에 대한 응답 데이터를 담고 있는 객체.")
    public static class Register {
        @Schema(name = "회원가입 성공여부", description = "회원가입이 성공했는지 여부.")
        private boolean isSuccess;

        @Schema(name = "전달 메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }

    @Getter
    @Builder
    @Schema(name = "중복확인 응답 DTO", description = "중복확인 요청에 대한 응답 데이터를 담고 있는 객체.")
    public static class CheckDuplication {
        @Schema(name = "사용가능 여부", description = "해당 파라미터가 사용가능한지 여부.")
        private boolean isOk;

        @Schema(name = "전달 메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }

    @Getter
    @Builder
    @Schema(name = "회원 활성화 응답 DTO", description = "회원 활성화 요청에 대한 응답 데이터를 담고 있는 객체.")
    public static class Activate {
        @Schema(name = "활성화 성공여부", description = "회원 활성화가 성공했는지 여부.")
        private boolean isSuccess;

        @Schema(name = "전달 메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }

    @Getter
    @Builder
    @Schema(name = "회원탈퇴 응답 DTO", description = "회원탈퇴 요청에 대한 응답 데이터를 담고 있는 객체.")
    public static class Delete {
        @Schema(name = "회원탈퇴 성공여부", description = "회원탈퇴가 성공했는지 여부.")
        private boolean isSuccess;

        @Schema(name = "전달 메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }

    @Getter
    @Builder
    @Schema(name = "비밀번호 변경 응답 DTO", description = "비밀번호 변경 요청 응답에 필요한 데이터를 담고 있는 객체.")
    public static class ChangePassword {
        @Schema(name = "비밀번호 변경 성공여부", description = "비밀번호 변경이 성공했는지 여부.")
        private boolean isSuccess;

        @Schema(name = "전달 메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }
}
