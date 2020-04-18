package movieland.errors.notfound;

import movieland.constants.entities.GenreConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = GenreConstants.GENRE_NOT_FOUND)
public class GenreNotFoundException extends BaseHttpException {

    public GenreNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
