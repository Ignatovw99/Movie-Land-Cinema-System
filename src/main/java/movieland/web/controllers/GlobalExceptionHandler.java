package movieland.web.controllers;

import movieland.errors.BaseHttpException;
import movieland.errors.invalid.SeatNotFreeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(BaseHttpException.class)
    public ModelAndView handleHttpException(BaseHttpException httpException) {
        return viewHttpException(httpException);
    }

    @ExceptionHandler(SeatNotFreeException.class)
    public ResponseEntity<String> handleSeatNotFreeException(SeatNotFreeException seatNotFreeException) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(seatNotFreeException.getMessage());
    }
}
