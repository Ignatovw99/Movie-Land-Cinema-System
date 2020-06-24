package movieland.errors.duplicate;

import movieland.constants.entities.CinemaConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = CinemaConstants.CINEMA_ALREADY_EXISTS)
public class CinemaAlreadyExistsException extends ConflictException {

    public CinemaAlreadyExistsException(String message) {
        super(message);
    }
}
