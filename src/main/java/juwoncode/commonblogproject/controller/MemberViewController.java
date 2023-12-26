package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberViewController {
    private MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "member/login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "member/register";
    }
}
