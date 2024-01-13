package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.dto.ApiResponse;
import juwoncode.commonblogproject.service.MemberService;
import org.springframework.web.bind.annotation.*;

import static juwoncode.commonblogproject.vo.ResponseMessage.*;
import static juwoncode.commonblogproject.vo.HttpCode.*;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {
    private MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register/check-username/{username}")
    public ApiResponse.OnlyMessageDto callCheckUsernameService(@PathVariable String username) {
        if (!memberService.checkUsername(username)) {
            return new ApiResponse.OnlyMessageDto(HTTP_STATUS_CONFLICT, USERNAME_DUPLICATE_MESSAGE);
        }

        return new ApiResponse.OnlyMessageDto(HTTP_STATUS_OK, USERNAME_NOT_DUPLICATE_MESSAGE);
    }

    @GetMapping("/register/check-email/{email}")
    public ApiResponse.OnlyMessageDto callCheckEmailService(@PathVariable String email) {
        if (!memberService.checkEmail(email)) {
            return new ApiResponse.OnlyMessageDto(HTTP_STATUS_CONFLICT, EMAIL_DUPLICATE_MESSAGE);
        }

        return new ApiResponse.OnlyMessageDto(HTTP_STATUS_OK, EMAIL_NOT_DUPLICATE_MESSAGE);
    }
}
