package com.github.mhdirkse.timewriter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GreetingController {
    @GetMapping("/hello")
    public ModelAndView greet() {
        ModelAndView result = new ModelAndView("hello");
        result.getModel().put("name", "Martijn");
        return result;
    }
}
