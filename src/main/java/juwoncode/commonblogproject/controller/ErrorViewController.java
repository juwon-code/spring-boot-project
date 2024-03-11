package juwoncode.commonblogproject.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorViewController implements ErrorController {
    @GetMapping("/400")
    public String get400Page() {
        return "/error/400";
    }

    @GetMapping("/404")
    public String get404Page() {
        return "/error/404";
    }
}
