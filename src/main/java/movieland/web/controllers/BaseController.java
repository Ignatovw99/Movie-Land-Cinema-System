package movieland.web.controllers;

import movieland.constants.GlobalConstants;
import movieland.errors.BaseHttpException;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    protected BaseController() {
    }

    ModelAndView view() {
        return new ModelAndView();
    }

    ModelAndView view(Object viewModel) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(GlobalConstants.MODEL_NAME, viewModel);
        return modelAndView;
    }

    ModelAndView redirect(String route) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:" + route);
        return modelAndView;
    }

    ModelAndView viewHttpException(BaseHttpException httpException) {
        ModelAndView andView = new ModelAndView("fragments/layout");
        andView.addObject("view", "error/error");
        andView.addObject("error", httpException);
        andView.setStatus(httpException.getHttpStatus());
        return andView;
    }
}
