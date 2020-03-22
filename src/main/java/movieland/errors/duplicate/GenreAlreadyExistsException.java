package movieland.errors.duplicate;

import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//TODO: add to constants class
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Genre already exists")
public class GenreAlreadyExistsException extends BaseHttpException {

    public GenreAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
