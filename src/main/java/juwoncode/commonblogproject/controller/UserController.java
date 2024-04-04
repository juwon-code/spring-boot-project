package juwoncode.commonblogproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import juwoncode.commonblogproject.dto.ApiResponseDto;
import juwoncode.commonblogproject.dto.UserRequestDto;
import juwoncode.commonblogproject.dto.UserResponseDto;
import juwoncode.commonblogproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "회원 API", description = "회원과 관련된 기능을 수행하는 API를 설명하는 문서.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    @Operation(summary = "(GET) 로그인 페이지 호출", description = "로그인 페이지 뷰를 반환한다.")
    @Parameter(name = "error", description = "로그인 실패 이유를 담는 문자열.")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "user/login";
    }

    @GetMapping("/user/register")
    @Operation(summary = "(GET) 회원가입 페이지 호출", description = "회원가입 페이지 뷰를 반환한다.")
    public String getRegisterPage() {
        return "user/register";
    }

    @GetMapping("/user/change-password")
    @Operation(summary = "(GET) 비밀번호 변경 페이지 호출", description = "비밀번호 변경 페이지 뷰를 반환한다.")
    public String getChangePasswordPage() {
        return "user/support/change-password";
    }

    @ResponseBody
    @PostMapping("/api/v1/user/register")
    @Operation(summary = "(POST) 회원가입", description = "회원가입을 처리하고, 성공여부를 반환한다.")
    public ApiResponseDto.WithBody<?> register(@ModelAttribute UserRequestDto.Register requestDto) {
        UserResponseDto.Register responseDto = userService.register(requestDto);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("회원가입 요청이 완료되었습니다.")
                .body(responseDto)
                .build();
    }

    @ResponseBody
    @GetMapping("/api/v1/user/check-username/{username}")
    @Operation(summary = "(GET) 아이디 중복검사", description = "아이디 중복을 검사하고, 중복여부를 반환한다.")
    @Parameter(name = "username", description = "중복을 검사할 아이디.")
    public ApiResponseDto.WithBody<?> checkUsernameDuplicated(@PathVariable String username) {
        UserResponseDto.CheckDuplication responseDto = userService.checkUsernameDuplicated(username);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("아이디 중복검사가 완료되었습니다.")
                .body(responseDto)
                .build();
    }

    @ResponseBody
    @GetMapping("/api/v1/user/check-email/{email}")
    @Operation(summary = "(GET) 메일주소 중복검사", description = "메일주소 중복을 검사하고, 중복여부를 반환한다.")
    @Parameter(name = "email", description = "중복을 검사할 메일주소.")
    public ApiResponseDto.WithBody<?> checkEmailDuplicated(@PathVariable String email) {
        UserResponseDto.CheckDuplication responseDto = userService.checkEmailDuplicated(email);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("메일주소 중복검사가 완료되었습니다.")
                .body(responseDto)
                .build();
    }

    @ResponseBody
    @GetMapping("/api/v1/user/activate")
    @Operation(summary = "(GET) 회원 활성화", description = "회원을 활성화하고, 성공여부를 반환한다.")
    public ApiResponseDto.WithBody<?> activate(@ModelAttribute UserRequestDto.Activate requestDto) {
        UserResponseDto.Activate responseDto = userService.activate(requestDto);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("회원 활성화 요청이 완료되었습니다.")
                .body(responseDto)
                .build();
    }

    @ResponseBody
    @PostMapping("/api/v1/user/delete")
    @Operation(summary = "(POST) 회원 탈퇴", description = "회원을 탈퇴하고, 성공여부를 반환한다.")
    public ApiResponseDto.WithBody<?> delete(@ModelAttribute UserRequestDto.Delete requestDto) {
        UserResponseDto.Delete responseDto = userService.delete(requestDto);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("회원 삭제 요청이 완료되었습니다.")
                .body(responseDto)
                .build();
    }

    @ResponseBody
    @PostMapping("/api/v1/user/support/change-password")
    @Operation(summary = "(POST) 비밀번호 변경", description = "비밀번호를 변경하고, 성공여부를 반환한다.")
    public ApiResponseDto.WithBody<?> changePassword(UserRequestDto.ChangePassword requestDto) {
        UserResponseDto.ChangePassword responseDto = userService.changePassword(requestDto);

        return ApiResponseDto.WithBody.builder()
                .status(HttpStatus.OK.value())
                .message("비밀번호 변경 요청이 완료되었습니다.")
                .body(responseDto)
                .build();
    }
}
