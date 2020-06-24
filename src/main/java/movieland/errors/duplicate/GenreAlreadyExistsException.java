package movieland.errors.duplicate;

import movieland.constants.entities.GenreConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = GenreConstants.GENRE_ALREADY_EXISTS)
public class GenreAlreadyExistsException extends ConflictException {

    public GenreAlreadyExistsException(String message) {
        super(message);
    }
}
