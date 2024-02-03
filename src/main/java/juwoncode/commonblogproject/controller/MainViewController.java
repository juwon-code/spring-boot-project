package juwoncode.commonblogproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {
    /**
     * 홈페이지를 반환한다.
     * @return
     *      홈페이지 뷰.
     */
    @GetMapping({"/", "/home"})
    public String getHomePage() {
        return "home";
    }
}
