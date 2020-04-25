package movieland.errors.invalid;

import movieland.constants.entities.CinemaConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = CinemaConstants.INVALID_CINEMA_MODEL)
public class InvalidCinemaException extends BaseHttpException {

    public InvalidCinemaException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
