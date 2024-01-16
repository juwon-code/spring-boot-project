package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.config.SecurityConfig;
import juwoncode.commonblogproject.service.MemberDetailsService;
import juwoncode.commonblogproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static juwoncode.commonblogproject.vo.HttpCode.*;
import static juwoncode.commonblogproject.vo.ResponseMessage.*;

@ImportAutoConfiguration(SecurityConfig.class)
@WebMvcTest(MemberApiController.class)
public class MemberApiControllerTests {
    @MockBean
    MemberService memberService;
    @MockBean
    MemberDetailsService memberDetailsService;
    @Autowired
    MockMvc mockMvc;

    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_callCheckUsernameService_when_success() throws Exception {
        String username = "username";

        when(memberService.checkUsernameDuplicated(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/member/register/check-username/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_OK))
                .andExpect(jsonPath("$.message").value(USERNAME_NOT_DUPLICATE_MESSAGE));

        verify(memberService).checkUsernameDuplicated(anyString());
    }

    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_callCheckUsernameService_when_failure() throws Exception {
        String username = "username";

        when(memberService.checkUsernameDuplicated(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/member/register/check-username/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_CONFLICT))
                .andExpect(jsonPath("$.message").value(USERNAME_DUPLICATE_MESSAGE));

        verify(memberService).checkUsernameDuplicated(anyString());
    }

    @DisplayName("이메일 중복 검사 서비스 호출 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_callCheckEmailService_when_success() throws Exception {
        String email = "aa@aa.aa";

        when(memberService.checkEmailDuplicated(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/member/register/check-email/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_OK))
                .andExpect(jsonPath("$.message").value(EMAIL_NOT_DUPLICATE_MESSAGE));

        verify(memberService).checkEmailDuplicated(anyString());
    }

    @DisplayName("이메일 중복 검사 서비스 호출 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_callCheckEmailService_when_failure() throws Exception {
        String email = "aa@aa.aa";

        when(memberService.checkEmailDuplicated(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/member/register/check-email/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_CONFLICT))
                .andExpect(jsonPath("$.message").value(EMAIL_DUPLICATE_MESSAGE));

        verify(memberService).checkEmailDuplicated(anyString());
    }
}
