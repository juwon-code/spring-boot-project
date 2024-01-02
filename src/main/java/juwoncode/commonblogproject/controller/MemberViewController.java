package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.request.MemberRequest;
import juwoncode.commonblogproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/register")
    public String callRegisterService(@RequestBody MemberRequest.RegisterDto dto, RedirectAttributes attributes) {
        if (memberService.register(dto)) {
            attributes.addFlashAttribute("message","회원가입이 성공했습니다!");
            return "redirect:/member/validate/email";
        }

        attributes.addFlashAttribute("message", "회원가입이 실패했습니다!");
        return "redirect:/member/register";
    }

    @PostMapping("/change/password")
    public String callModifyService(@RequestBody MemberRequest.ChangePasswordDto dto, RedirectAttributes attributes) {
        if (memberService.changePassword(dto)) {
            attributes.addFlashAttribute("message", "비밀번호 변경이 성공했습니다! 다시 로그인해주세요.");
            return "redirect:/member/login";
        }

        attributes.addFlashAttribute("message", "비밀번호 변경이 실패했습니다!");
        return "redirect:/member/change/password";
    }
}
