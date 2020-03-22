package movieland.errors;

public class BaseHttpException extends RuntimeException {

    private final int httpStatus;

    protected BaseHttpException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
