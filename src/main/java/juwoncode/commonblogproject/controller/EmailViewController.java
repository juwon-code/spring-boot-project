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

    @GetMapping("/verify/result")
    public String getResultPage() {
        return "email/verify/result";
    }

    @GetMapping("/verify/success")
    public String getSuccessPage() {
        return "email/verify/success";
    }

    @GetMapping("/verify/error")
    public String getErrorPage() {
        return "email/verify/error";
    }

    @PostMapping("/verify/send")
    public String callSendVerifyMailService(SendDto dto) {
        emailService.sendVerifyMail(dto);

        return "redirect:/email/verify/result";
    }

    @GetMapping("/verify/check")
    public String callCheckVerifyMailService(CheckDto dto) {
        if (!emailService.checkVerifyMail(dto)) {
            return "redirect:/email/verify/error";
        }

        return "redirect:/email/verify/success";
    }
}
