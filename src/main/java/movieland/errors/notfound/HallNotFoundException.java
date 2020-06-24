package movieland.errors.notfound;

import movieland.constants.entities.HallConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = HallConstants.HALL_NOT_FOUND)
public class HallNotFoundException extends NotFoundException {

    public HallNotFoundException(String message) {
        super(message);
    }
}
