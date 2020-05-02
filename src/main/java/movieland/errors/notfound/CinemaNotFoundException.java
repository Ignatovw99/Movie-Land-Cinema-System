package movieland.errors.notfound;

import movieland.constants.entities.CinemaConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = CinemaConstants.CINEMA_NOT_FOUND)
public class CinemaNotFoundException extends BaseHttpException {

    public CinemaNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
