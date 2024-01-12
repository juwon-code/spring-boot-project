package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;

@Valid
public class ApiResponse<T> {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class OnlyMessageDto {
        @NotEmpty(message = HTTP_CODE_EMPTY_EXCEPTION)
        private int code;
        @NotEmpty(message = API_MESSAGE_EMPTY_EXCEPTION)
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class WithBodyDto<T> {
        @NotEmpty(message = HTTP_CODE_EMPTY_EXCEPTION)
        private int code;
        @NotEmpty(message = API_MESSAGE_EMPTY_EXCEPTION)
        private String message;
        @NotEmpty(message = API_BODY_EMPTY_EXCEPTION)
        private T body;
    }
}
