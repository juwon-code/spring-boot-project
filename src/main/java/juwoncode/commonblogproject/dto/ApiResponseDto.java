package juwoncode.commonblogproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Valid
public class ApiResponseDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "API 응답 DTO (메시지)", description = "API 응답에 필요한 코드와 메시지를 담고 있는 객체.")
    public static class WithMessage {
        @NotNull(message = "응답코드 파라미터가 비어있습니다.")
        @Schema(name = "응답코드", description = "HTTP 응답 코드.")
        private int status;

        @NotNull(message = "응답메시지 파라미터가 비어있습니다.")
        @Schema(name = "응답메시지", description = "클라이언트에 전달할 메시지.")
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "API 응답 DTO (바디)", description = "API 응답에 필요한 코드, 메시지, 바디를 담고 있는 객체.")
    public static class WithBody<T> {
        @NotNull(message = "응답코드 파라미터가 비어있습니다.")
        @Schema(name = "응답코드", description = "HTTP 응답 코드.")
        private int status;

        @NotNull(message = "응답메시지 파라미터가 비어있습니다.")
        @Schema(name = "응답메시지", description = "클라이언트에 전달할 메시지.")
        private String message;

        @NotEmpty(message = "응답바디 파라미터가 비어있습니다.")
        @Schema(name = "응답바디", description = "클라이언트에 전달할 객체.")
        private T body;
    }
}
