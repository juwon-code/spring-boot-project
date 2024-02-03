package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static juwoncode.commonblogproject.dto.EmailRequest.*;

@Controller
@RequestMapping("/email")
public class EmailViewController {
    private final EmailService emailService;

    public EmailViewController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * 인증 메일 전송 완료 페이지를 반환한다.
     * @return
     *      인증 메일 전송 완료 페이지 뷰.
     */
    @GetMapping("/verify/result")
    public String getResultPage() {
        return "email/verify/result";
    }

    /**
     * 인증 메일 성공 페이지를 반환한다.
     * @return
     *      인증 메일 성공 페이지 뷰.
     */
    @GetMapping("/verify/success")
    public String getSuccessPage() {
        return "email/verify/success";
    }

    /**
     * 인증 메일 에러 페이지를 반환한다.
     * @return
     *      인증 메일 에러 페이지 뷰.
     */
    @GetMapping("/verify/error")
    public String getErrorPage() {
        return "email/verify/error";
    }

    /**
     * 인증 메일 전송 서비스를 호출한다.
     * @param dto
     *      인증 메일 전송 요청 데이터를 담고 있는 DTO 객체.
     * @return
     *      인증 메일 전송 완료 페이지로 리다이렉션을 수행한다.
     */
    @PostMapping("/verify/send")
    public String callSendVerifyMailService(SendDto dto) {
        emailService.sendVerifyMail(dto);

        return "redirect:/email/verify/result";
    }
}
