package movieland.errors.invalid;

import movieland.constants.entities.ProjectionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = ProjectionConstants.PROJECTION_MODEL_NOT_VALID)
public class InvalidProjectionException extends BadRequestException {

    public InvalidProjectionException(String message) {
        super(message);
    }
}
