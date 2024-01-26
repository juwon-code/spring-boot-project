package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;

@Valid
public class ApiResponse {
    /**
     * API 응답 파라미터로 메시지만을 담고있는 DTO 클래스.
     * <pre>
     * - code: HTTP 코드, 비워둘 수 없음.
     * - message: 응답 메시지, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class OnlyMessageDto {
        @NotEmpty(message = HTTP_CODE_EMPTY_EXCEPTION)
        private int code;

        @NotEmpty(message = API_MESSAGE_EMPTY_EXCEPTION)
        private String message;
    }

    /**
     * API 응답 파라미터로 바디를 포함하는 DTO 클래스.
     * @param <T>
     *     제네릭 클래스.
     * <pre>
     * - code: HTTP 코드, 비워둘 수 없음.
     * - message: 응답 메시지, 비워둘 수 없음.
     * - body: 응답 바디, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     */
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
