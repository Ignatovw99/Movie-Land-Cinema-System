package movieland.web.controllers;

import movieland.providers.ViewNameProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    @Autowired
    private ViewNameProvider viewNameProvider;

    protected BaseController() {
    }

    private StackTraceElement getControllerAndActionStackTraceElement() {
        return Thread.currentThread()
                .getStackTrace()[4];
    }

    private ModelAndView getModelAndViewWithViewName() {
        ModelAndView modelAndView = new ModelAndView();
        String viewName =
                viewNameProvider.getControllerName(getControllerAndActionStackTraceElement()) + "-" + viewNameProvider.getMethodName(getControllerAndActionStackTraceElement());

        modelAndView.setViewName("fragments/layout");
        modelAndView.addObject("view", viewName);
        return modelAndView;
    }

    ModelAndView view() {
        return getModelAndViewWithViewName();
    }

    ModelAndView view(Object viewModel) {
        ModelAndView modelAndView = getModelAndViewWithViewName();
        modelAndView.addObject("model", viewModel);
        return modelAndView;
    }

    ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("fragments/layout");
        modelAndView.addObject("view", viewName);
        return modelAndView;
    }

    ModelAndView view(String viewName, Object viewModel) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("fragments/layout");
        modelAndView.addObject("view", viewName);
        modelAndView.addObject("model", viewModel);
        return modelAndView;
    }

    ModelAndView redirect(String route) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:" + route);
        return modelAndView;
    }
}
