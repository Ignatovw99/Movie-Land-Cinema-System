package movieland.errors.duplicate;

import movieland.constants.entities.MovieConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = MovieConstants.MOVIE_ALREADY_EXISTS)
public class MovieAlreadyExistsException extends ConflictException {

    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}
