package com.github.mhdirkse.timewriter;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GreetingController {
    @GetMapping("/hello")
    public ModelAndView greet(@AuthenticationPrincipal UserPrincipal user) {
        ModelAndView result = new ModelAndView("hello");
        result.getModel().put("name", user.getUsername());
        return result;
    }
}
