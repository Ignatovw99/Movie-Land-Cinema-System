package movieland.errors.notfound;

import movieland.constants.entities.ProgrammeConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ProgrammeConstants.PROGRAMME_NOT_FOUND)
public class ProgrammeNotFoundException extends NotFoundException {

    public ProgrammeNotFoundException(String message) {
        super(message);
    }
}
