package movieland.errors.invalid;

import movieland.constants.entities.HallConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = HallConstants.HALL_CAN_NOT_CHANGE_ITS_CINEMA)
public class HallCinemaNotChangeableException extends BaseHttpException {

    public HallCinemaNotChangeableException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
