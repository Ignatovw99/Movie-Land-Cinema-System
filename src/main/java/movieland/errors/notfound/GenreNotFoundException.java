package movieland.errors.notfound;

import movieland.constants.entities.GenreConstants;
import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = GenreConstants.GENRE_NOT_FOUND)
public class GenreNotFoundException extends NotFoundException {

    public GenreNotFoundException(String message) {
        super(message);
    }
}
