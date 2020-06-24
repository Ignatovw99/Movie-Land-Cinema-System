package movieland.errors.notfound;

import movieland.constants.entities.CinemaConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = CinemaConstants.CINEMA_NOT_FOUND)
public class CinemaNotFoundException extends NotFoundException {

    public CinemaNotFoundException(String message) {
        super(message);
    }
}
