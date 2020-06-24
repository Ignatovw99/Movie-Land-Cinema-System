package movieland.errors;

import org.springframework.http.HttpStatus;

public class BaseHttpException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected BaseHttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
