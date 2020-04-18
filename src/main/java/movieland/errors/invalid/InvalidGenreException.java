package movieland.errors.invalid;

import movieland.constants.entities.GenreConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = GenreConstants.INVALID_GENRE_MODEL)
public class InvalidGenreException extends BaseHttpException {

    public InvalidGenreException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
