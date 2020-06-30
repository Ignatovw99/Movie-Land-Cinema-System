package movieland.errors.invalid;

import movieland.constants.entities.ProgrammeConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = ProgrammeConstants.PROGRAMME_MODEL_NOT_VALID)
public class InvalidProgrammeException extends BadRequestException {

    public InvalidProgrammeException(String message) {
        super(message);
    }
}
