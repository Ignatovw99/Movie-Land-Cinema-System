package movieland.errors.invalid;

import movieland.errors.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//TODO: extend to constant class reason
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Genre Model")
public class InvalidGenreModelException extends BaseHttpException {

    public InvalidGenreModelException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
