package movieland.errors.invalid;

import movieland.constants.entities.HallConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = HallConstants.HALL_CAN_NOT_CHANGE_ITS_CINEMA)
public class HallCinemaNotChangeableException extends BadRequestException {

    public HallCinemaNotChangeableException(String message) {
        super(message);
    }
}
