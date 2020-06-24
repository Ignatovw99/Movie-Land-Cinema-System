package movieland.errors.duplicate;

import movieland.constants.entities.HallConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = HallConstants.HALL_ALREADY_EXISTS_IN_THE_GIVEN_CINEMA)
public class HallAlreadyExistsException extends ConflictException {

    public HallAlreadyExistsException(String message) {
        super(message);
    }
}
