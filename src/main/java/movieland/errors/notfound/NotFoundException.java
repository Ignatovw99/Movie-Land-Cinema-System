package movieland.errors.notfound;

import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseHttpException {

    protected NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
