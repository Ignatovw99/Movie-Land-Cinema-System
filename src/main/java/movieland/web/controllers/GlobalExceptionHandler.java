package movieland.web.controllers;

import movieland.errors.BaseHttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(BaseHttpException.class)
    public ModelAndView handleHttpException(BaseHttpException httpException) {
        return viewHttpException(httpException);
    }
}
