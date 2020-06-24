package movieland.errors.invalid;

import movieland.constants.entities.GenreConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = GenreConstants.INVALID_GENRE_MODEL)
public class InvalidGenreException extends BadRequestException {

    public InvalidGenreException(String message) {
        super(message);
    }
}
