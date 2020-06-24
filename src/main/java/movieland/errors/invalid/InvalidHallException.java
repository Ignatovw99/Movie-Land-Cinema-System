package movieland.errors.invalid;

import movieland.constants.entities.HallConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = HallConstants.INVALID_HALL_MODEL)
public class InvalidHallException extends BadRequestException {

    public InvalidHallException(String message) {
        super(message);
    }
}
