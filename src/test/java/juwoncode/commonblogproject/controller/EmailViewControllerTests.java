package juwoncode.commonblogproject.controller;

import juwoncode.commonblogproject.config.SecurityConfig;
import juwoncode.commonblogproject.dto.EmailRequest;
import juwoncode.commonblogproject.exception.AuthExceptionHandler;
import juwoncode.commonblogproject.service.EmailService;
import juwoncode.commonblogproject.service.MemberDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.any;
import static juwoncode.commonblogproject.dto.EmailRequest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ImportAutoConfiguration(SecurityConfig.class)
@WebMvcTest(EmailViewController.class)
public class EmailViewControllerTests {
    @MockBean
    EmailService emailService;

    @MockBean
    MemberDetailsService memberDetailsService;

    @MockBean
    AuthExceptionHandler authExceptionHandler;

    @Autowired
    MockMvc mockMvc;

    /**
     * 인증메일 전송 서비스 호출 메소드의 성공 케이스를 테스트한다.<br>
     * {@link EmailService#sendVerifyMail(SendDto)}을 수행하고 인증메일 전송 완료 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("인증메일 송신 서비스 호출 테스트")
    @Test
    @WithAnonymousUser
    void test_callSendVerifyMailService_when_success() throws Exception {
        doNothing().when(emailService).sendVerifyMail(any(SendDto.class));

        mockMvc.perform(post("/email/verify/send")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/email/verify/result"));

        verify(emailService).sendVerifyMail(any(SendDto.class));
    }

    /**
     * 인증메일 전송 서비스를 호출하는 메소드의 성공 케이스를 테스트한다.<br>
     * {@link EmailService#checkVerifyMail(CheckDto)}에서 true를 반환할 때,
     * 성공 메시지와 함께 인증메일 성공 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("인증메일 확인 서비스 호출 테스트 (성공)")
    @Test
    @WithAnonymousUser
    void test_callCheckVerifyMailService_when_success() throws Exception {
        when(emailService.checkVerifyMail(any(CheckDto.class))).thenReturn(true);

        mockMvc.perform(get("/email/verify/check")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/email/verify/success"));

        verify(emailService).checkVerifyMail(any(CheckDto.class));
    }

    /**
     * 인증메일 전송 서비스를 호출하는 메소드의 실패 케이스를 테스트한다.<br>
     * {@link EmailService#checkVerifyMail(CheckDto)}에서 false를 반환할 때,
     * 성공 메시지와 함께 인증메일 성공 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("인증메일 확인 서비스 호출 테스트 (실패)")
    @Test
    @WithAnonymousUser
    void test_callCheckVerifyMailService_when_failure() throws Exception {
        when(emailService.checkVerifyMail(any(CheckDto.class))).thenReturn(false);

        mockMvc.perform(get("/email/verify/check")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/email/verify/error"));

        verify(emailService).checkVerifyMail(any(CheckDto.class));
    }
}
