package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {
    private MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register/check-register/{username}")
    public ResponseEntity<String> checkRegister(@PathVariable String username) {
        if (memberService.checkUsername(username)) {
            return new ResponseEntity<>("사용가능한 아이디 입니다.", HttpStatus.OK);
        }

        return new ResponseEntity<>("중복된 회원 아이디 입니다.", HttpStatus.OK);
    }
}
