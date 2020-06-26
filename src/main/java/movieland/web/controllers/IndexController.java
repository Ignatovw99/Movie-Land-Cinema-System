package movieland.web.controllers;

import movieland.web.annotations.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    @Page(name = "index")
    public ModelAndView index(ModelAndView modelAndView) {
        return modelAndView;
    }
}
