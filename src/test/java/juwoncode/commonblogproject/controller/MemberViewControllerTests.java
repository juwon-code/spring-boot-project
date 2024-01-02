package juwoncode.commonblogproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import juwoncode.commonblogproject.request.MemberRequest;
import juwoncode.commonblogproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberViewController.class)
public class MemberViewControllerTests {
    @MockBean
    MemberService memberService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("회원가입 서비스 호출 테스트 (성공)")
    @Test
    void test_callRegisterService_when_success() throws Exception {
        MemberRequest.RegisterDto dto =
                new MemberRequest.RegisterDto("username", "password", "username@email.com");
        given(memberService.register(any(MemberRequest.RegisterDto.class))).willReturn(true);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/validate/email"))
                .andExpect(flash().attribute("message", "회원가입이 성공했습니다!"));
    }

    @DisplayName("회원가입 서비스 호출 테스트 (실패)")
    @Test
    void test_callRegisterService_when_failure() throws Exception {
        MemberRequest.RegisterDto dto =
                new MemberRequest.RegisterDto("username", "password", "username@email.com");
        given(memberService.register(any(MemberRequest.RegisterDto.class))).willReturn(false);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/register"))
                .andExpect(flash().attribute("message", "회원가입이 실패했습니다!"));
    }

    @DisplayName("비밀번호 수정 서비스 호출 테스트 (성공)")
    @Test
    void test_callChangePasswordService_when_success() throws Exception {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "original", "change");
        given(memberService.changePassword(any(MemberRequest.ChangePasswordDto.class))).willReturn(true);

        mockMvc.perform(post("/member/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/login"))
                .andExpect(flash().attribute("message", "비밀번호 변경이 성공했습니다! 다시 로그인해주세요."));
    }

    @DisplayName("비밀번호 수정 서비스 호출 테스트 (실패)")
    @Test
    void test_callChangePasswordService_when_failure() throws Exception {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "original", "change");
        given(memberService.changePassword(any(MemberRequest.ChangePasswordDto.class))).willReturn(false);

        mockMvc.perform(post("/member/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/change/password"))
                .andExpect(flash().attribute("message", "비밀번호 변경이 실패했습니다!"));
    }

}
