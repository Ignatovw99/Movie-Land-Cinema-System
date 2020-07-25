package movieland.web.controllers;

import movieland.errors.BaseHttpException;
import org.springframework.web.servlet.ModelAndView;

import static movieland.constants.GlobalConstants.*;

public class BaseController {

    protected BaseController() {
    }

    ModelAndView view() {
        return new ModelAndView();
    }

    ModelAndView view(Object viewModel) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MODEL_NAME, viewModel);
        return modelAndView;
    }

    ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(BASE_VIEW_TEMPLATE);
        modelAndView.addObject(VIEW_TEMPLATE_KEY, viewName);
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
