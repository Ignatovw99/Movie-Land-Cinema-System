package movieland.errors.invalid;

import movieland.constants.entities.MovieConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MovieConstants.INVALID_MOVIE_MODEL)
public class InvalidMovieException extends BaseHttpException {

    public InvalidMovieException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
