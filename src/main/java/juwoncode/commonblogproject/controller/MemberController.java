package juwoncode.commonblogproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "member/login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "member/register";
    }
}
