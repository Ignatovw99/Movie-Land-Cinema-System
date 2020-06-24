package movieland.errors.invalid;

import movieland.constants.entities.MovieConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MovieConstants.INVALID_MOVIE_MODEL)
public class InvalidMovieException extends BadRequestException {

    public InvalidMovieException(String message) {
        super(message);
    }
}
