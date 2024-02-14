package juwoncode.commonblogproject.exception;

import juwoncode.commonblogproject.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse.OnlyMessageDto processValidationError(MethodArgumentNotValidException exception) {
        String message = exception.getMessage();
        return new ApiResponse.OnlyMessageDto(HttpStatus.BAD_REQUEST.value(), message);
    }
}
