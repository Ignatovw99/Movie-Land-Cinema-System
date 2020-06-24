package movieland.errors.invalid;

import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseHttpException {

    protected BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
