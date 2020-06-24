package movieland.errors.notfound;

import movieland.constants.entities.MovieConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = MovieConstants.MOVIE_NOT_FOUND)
public class MovieNotFoundException extends NotFoundException {

    public MovieNotFoundException(String message) {
        super(message);
    }
}
