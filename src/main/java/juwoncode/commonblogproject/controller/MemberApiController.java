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

    /**
     * 회원명 중복 확인 서비스를 호출한다.
     * @param username
     *      회원명.
     * @return
     *      중복되지 않을 경우 상태 코드 200과 성공 메시지를 담고, 중복될 경우 상태 코드 409와 실패 메시지를 반환한다.
     */
    @GetMapping("/register/check-username/{username}")
    public ApiResponse.OnlyMessageDto callCheckUsernameService(@PathVariable String username) {
        if (!memberService.checkUsernameDuplicated(username)) {
            return new ApiResponse.OnlyMessageDto(HTTP_STATUS_CONFLICT, USERNAME_DUPLICATE_MESSAGE);
        }

        return new ApiResponse.OnlyMessageDto(HTTP_STATUS_OK, USERNAME_NOT_DUPLICATE_MESSAGE);
    }

    /**
     * 이메일 중복 확인 서비스를 호출한다.
     * @param email
     *      이메일.
     * @return
     *      중복되지 않을 경우 상태 코드 200과 성공 메시지를 담고, 중복될 경우 상태 코드 409와 실패 메시지를 반환한다.
     */
    @GetMapping("/register/check-email/{email}")
    public ApiResponse.OnlyMessageDto callCheckEmailService(@PathVariable String email) {
        if (!memberService.checkEmailDuplicated(email)) {
            return new ApiResponse.OnlyMessageDto(HTTP_STATUS_CONFLICT, EMAIL_DUPLICATE_MESSAGE);
        }

        return new ApiResponse.OnlyMessageDto(HTTP_STATUS_OK, EMAIL_NOT_DUPLICATE_MESSAGE);
    }
}
