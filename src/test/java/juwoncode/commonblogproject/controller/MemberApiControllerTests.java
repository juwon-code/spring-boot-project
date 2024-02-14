package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.config.SecurityConfig;
import juwoncode.commonblogproject.exception.AuthExceptionHandler;
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
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @MockBean
    AuthExceptionHandler authExceptionHandler;

    @Autowired
    MockMvc mockMvc;

    /**
     * 회원명 중복확인 서비스 호출 메소드의 성공 케이스를 테스트한다.<br>
     * {@link MemberService#checkUsernameDuplicated(String)}에서 true를 반환할 때, HTTP 코드(200)와 성공 메시지를 응답한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_callCheckUsernameService_when_success() throws Exception {
        String username = "username";

        when(memberService.checkUsernameDuplicated(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/member/register/check-username/" + username)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_OK))
                .andExpect(jsonPath("$.message").value(USERNAME_NOT_DUPLICATE_MESSAGE));

        verify(memberService).checkUsernameDuplicated(anyString());
    }

    /**
     * 회원명 중복확인 서비스 호출 메소드의 실패 케이스를 테스트한다.<br>
     * {@link MemberService#checkUsernameDuplicated(String)}에서 false를 반환할 때, HTTP 코드(409)와 실패 메시지를 응답한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("아이디 중복 검사 서비스 호출 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_callCheckUsernameService_when_failure() throws Exception {
        String username = "username";

        when(memberService.checkUsernameDuplicated(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/member/register/check-username/" + username)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_CONFLICT))
                .andExpect(jsonPath("$.message").value(USERNAME_DUPLICATE_MESSAGE));

        verify(memberService).checkUsernameDuplicated(anyString());
    }

    /**
     * 이메일 중복확인 서비스 호출 메소드의 성공 케이스를 테스트한다.<br>
     * {@link MemberService#checkEmailDuplicated(String)}에서 true를 반환할 때, HTTP 코드(200)와 성공 메시지를 응답한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("이메일 중복확인 서비스 호출 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_callCheckEmailService_when_success() throws Exception {
        String email = "aa@aa.aa";

        when(memberService.checkEmailDuplicated(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/member/register/check-email/" + email)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_OK))
                .andExpect(jsonPath("$.message").value(EMAIL_NOT_DUPLICATE_MESSAGE));

        verify(memberService).checkEmailDuplicated(anyString());
    }

    /**
     * 이메일 중복확인 서비스 호출 메소드의 실패 케이스를 테스트한다.<br>
     * {@link MemberService#checkEmailDuplicated(String)}에서 false를 반환할 때, HTTP 코드(409)와 실패 메시지를 응답한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("이메일 중복확인 서비스 호출 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_callCheckEmailService_when_failure() throws Exception {
        String email = "aa@aa.aa";

        when(memberService.checkEmailDuplicated(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/member/register/check-email/" + email)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HTTP_STATUS_CONFLICT))
                .andExpect(jsonPath("$.message").value(EMAIL_DUPLICATE_MESSAGE));

        verify(memberService).checkEmailDuplicated(anyString());
    }
}
