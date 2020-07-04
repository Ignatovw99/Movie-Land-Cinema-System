package movieland.errors.notfound;

import movieland.constants.entities.ProjectionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ProjectionConstants.PROJECTION_NOT_FOUND)
public class ProjectionNotFoundException extends NotFoundException {

    public ProjectionNotFoundException(String message) {
        super(message);
    }
}
