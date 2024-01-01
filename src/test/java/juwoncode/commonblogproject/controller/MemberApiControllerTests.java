package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberApiController.class)
public class MemberApiControllerTests {
    @MockBean
    MemberService memberService;
    @Autowired
    MockMvc mockMvc;

    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (성공)")
    @Test
    void test_checkRegister_when_success() throws Exception {
        String username = "username";

        when(memberService.checkUsername(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/member/register/check-register/" + username))
                .andExpect(status().isOk())
                .andExpect(content().string("사용가능한 아이디 입니다."));
    }

    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (실패)")
    @Test
    void test_checkRegister_when_failure() throws Exception {
        String username = "username";

        when(memberService.checkUsername(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/member/register/check-register/" + username))
                .andExpect(status().isOk())
                .andExpect(content().string("중복된 회원 아이디 입니다."));
    }
}
