package movieland.errors.duplicate;

import movieland.constants.entities.GenreConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = GenreConstants.GENRE_ALREADY_EXISTS)
public class GenreAlreadyExistsException extends BaseHttpException {

    public GenreAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
