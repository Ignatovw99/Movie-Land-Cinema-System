package movieland.errors.duplicate;

import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseHttpException {

    protected ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
