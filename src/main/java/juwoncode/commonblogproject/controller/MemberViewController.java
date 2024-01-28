package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static juwoncode.commonblogproject.vo.ResponseMessage.*;

@Controller
@RequestMapping("/member")
public class MemberViewController {
    private MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 로그인 페이지를 반환한다.
     * @param error
     *      로그인에서 발생한 오류 메시지.
     * @param model
     *      뷰에 오류 메시지를 전달할 Model 객체.
     * @return
     *      로그인 페이지 뷰.
     */
    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);

        return "member/login";
    }

    /**
     * 회원가입 페이지를 반환한다.
     * @return
     *      회원가입 페이지 뷰.
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "member/register";
    }

    /**
     * 비밀번호 변경 페이지를 반환한다.
     * @return
     *      비밀번호 변경 페이지 뷰.
     */
    @GetMapping("/change/password")
    public String getChangePasswordPage() {
        return "member/change/password";
    }

    /**
     * 회원가입 서비스를 호출한다.
     * @param dto
     *      회원가입 요청 데이터를 담고 있는 DTO 객체.
     * @param attributes
     *      리다이렉트에 플래시 메시지를 전달하기 위한 RedirectAttributes 객체.
     * @return
     *      회원가입이 성공하면 인증메일 전송 페이지로, 실패하면 회원가입 전송 페이지로 리다이렉션을 수행한다.
     */
    @PostMapping("/register")
    public String callRegisterService(MemberRequest.RegisterDto dto, RedirectAttributes attributes) {
        if (memberService.register(dto)) {
            attributes.addFlashAttribute("message", REGISTER_SUCCESS_MESSAGE);
            return "redirect:/member/validate/email";
        }

        attributes.addFlashAttribute("message", REGISTER_FAILURE_MESSAGE);
        return "redirect:/member/register";
    }

    /**
     * 비밀번호 변경 서비스를 호출한다.
     * @param dto
     *      비밀번호 변경 요청 데이터를 담고 있는 DTO 객체.
     * @param attributes
     *      리다이렉트에 플래시 메시지를 전달하기 위한 RedirectAttributes 객체.
     * @return
     *      비밀번호 변경이 성공하면 로그아웃을, 실패하면 비밀번호 변경 페이지로 리다이렉션을 수행한다.
     */
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
