package juwoncode.commonblogproject.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ApiResponse<T> {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class OnlyMessageDto {
        private int code;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class WithBodyDto<T> {
        private int code;
        private String message;
        private T body;
    }
}
