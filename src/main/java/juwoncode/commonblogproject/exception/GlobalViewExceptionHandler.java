package juwoncode.commonblogproject.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalViewExceptionHandler {
    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public String runtimeExceptionHandler(RuntimeException e) {
        return "redirect:/error/400";
    }
}
