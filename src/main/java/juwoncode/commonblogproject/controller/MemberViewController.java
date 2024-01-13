package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static juwoncode.commonblogproject.vo.ResponseMessage.*;

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

    @GetMapping("/change/password")
    public String getChangePasswordPage() {
        return "member/change/password";
    }

    @GetMapping("/validate/email")
    public String getValidateEmailPage() {
        return "member/validate/email";
    }

    @PostMapping("/register")
    public String callRegisterService(MemberRequest.RegisterDto dto, RedirectAttributes attributes) {
        if (memberService.register(dto)) {
            attributes.addFlashAttribute("message", REGISTER_SUCCESS_MESSAGE);
            return "redirect:/member/validate/email";
        }

        attributes.addFlashAttribute("message", REGISTER_FAILURE_MESSAGE);
        return "redirect:/member/register";
    }

    @PostMapping("/change/password")
    public String callModifyService(MemberRequest.ChangePasswordDto dto, RedirectAttributes attributes) {
        if (memberService.changePassword(dto)) {
            attributes.addFlashAttribute("message", CHANGE_PASSWORD_SUCCESS_MESSAGE);
            return "redirect:/member/logout";
        }

        attributes.addFlashAttribute("message", CHANGE_PASSWORD_FAILURE_MESSAGE);
        return "redirect:/member/change/password";
    }
}
