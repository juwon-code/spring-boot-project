package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.response.ApiResponse;
import juwoncode.commonblogproject.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {
    private MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register/check-username/{username}")
    public ApiResponse.OnlyMessageDto callCheckUsernameService(@PathVariable String username) {
        if (memberService.checkUsername(username)) {
            return new ApiResponse.OnlyMessageDto(HttpStatus.OK.value(), "사용가능한 아이디 입니다.");
        }

        return new ApiResponse.OnlyMessageDto(HttpStatus.CONFLICT.value(), "중복된 회원 아이디 입니다.");
    }

    @GetMapping("/register/check-email/{email}")
    public ApiResponse.OnlyMessageDto callCheckEmailService(@PathVariable String email) {
        if (memberService.checkEmail(email)) {
            return new ApiResponse.OnlyMessageDto(HttpStatus.OK.value(), "사용가능한 이메일 입니다.");
        }

        return new ApiResponse.OnlyMessageDto(HttpStatus.CONFLICT.value(), "중복된 이메일 입니다.");
    }
}
