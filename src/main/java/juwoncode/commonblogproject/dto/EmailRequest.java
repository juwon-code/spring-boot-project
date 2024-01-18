package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;

@Valid
public class EmailRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SendDto {
        @NotEmpty(message = EMAIL_EMPTY_EXCEPTION)
        @Email(message = EMAIL_WRONG_EXCEPTION)
        private String email;

        @NotEmpty(message = EMAIL_TYPE_EMPTY_EXCEPTION)
        private String type;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CheckDto {
        @NotEmpty(message = EMAIL_CODE_EMPTY_EXCEPTION)
        private String code;

        @NotEmpty(message = EMAIL_TYPE_EMPTY_EXCEPTION)
        private String type;
    }
}
