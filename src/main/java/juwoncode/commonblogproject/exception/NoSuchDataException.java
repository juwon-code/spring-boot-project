package juwoncode.commonblogproject.exception;

public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException() {
        super();
    }

    public NoSuchDataException(String message) {
        super(message);
    }

    public NoSuchDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
