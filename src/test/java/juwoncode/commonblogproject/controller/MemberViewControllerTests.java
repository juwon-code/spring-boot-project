package juwoncode.commonblogproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import juwoncode.commonblogproject.config.SecurityConfig;
import juwoncode.commonblogproject.dto.MemberDetails;
import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.exception.AuthExceptionHandler;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.service.MemberDetailsService;
import juwoncode.commonblogproject.service.MemberService;
import juwoncode.commonblogproject.vo.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static juwoncode.commonblogproject.vo.ResponseMessage.*;

@ImportAutoConfiguration(SecurityConfig.class)
@WebMvcTest(MemberViewController.class)
public class MemberViewControllerTests {
    @MockBean
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    MemberDetailsService memberDetailsService;

    @MockBean
    AuthExceptionHandler authExceptionHandler;

    @MockBean
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 시큐리티에서 설정한 폼 로그인의 성공 케이스를 테스트한다.<br>
     * 올바른 ID와 패스워드로 로그인을 시도할 때 인증이 성공한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("시큐리티 폼 로그인 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_formLoginFeature_when_success() throws Exception {
        String username = "username";
        String password = "password";

        MemberDetails fakeDetails = MemberDetails.builder()
                .username("username")
                .password("$2a$12$1CfuKx8v8UsaY1CeNPTAZ.Nq8S3TUpW1Uw1cAqln4uCz0yBJXjUk.")
                .email("username@email.com")
                .role(RoleType.valueOf("USER"))
                .enabled(true)
                .build();

        when(memberDetailsService.loadUserByUsername(anyString())).thenReturn(fakeDetails);

        mockMvc.perform(formLogin("/member/login")
                .user(username)
                .password(password)
        )
        .andExpect(authenticated());
    }

    /**
     * 시큐리티에서 설정한 폼 로그인의 실패 케이스를 테스트한다.<br>
     * 올바른 ID와 잘못된 패스워드로 로그인을 시도할 때 인증은 실패한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("시큐리티 폼 로그인 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_formLoginFeature_when_failure() throws Exception {
        String username = "username";
        String password = "password1";

        MemberDetails fakeDetails = MemberDetails.builder()
                .username("username")
                .password("$2a$12$1CfuKx8v8UsaY1CeNPTAZ.Nq8S3TUpW1Uw1cAqln4uCz0yBJXjUk.")
                .email("username@email.com")
                .role(RoleType.valueOf("USER"))
                .enabled(true)
                .build();

        when(memberDetailsService.loadUserByUsername(anyString())).thenReturn(fakeDetails);

        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(unauthenticated());
    }

    /**
     * 시큐리티에서 설정한 로그아웃의 실패 케이스를 테스트한다.<br>
     * 로그인된 가짜 회원을 로그아웃할 경우 로그인 페이지로 이동한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("시큐리티 폼 로그아웃 테스트 (성공)")
    @WithMockUser
    @Test
    void test_logoutFeature_when_failure() throws Exception {
        mockMvc.perform(logout("/member/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/login"));
    }

    /**
     * 회원가입 서비스 호출 메소드의 성공 케이스를 테스트한다.<br>
     * {@link MemberService#register(MemberRequest.RegisterDto)}에서 true를 반환할 때,
     * 성공 메시지와 함께 인증메일 전송 완료 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("회원가입 서비스 호출 테스트 (성공)")
    @WithAnonymousUser
    @Test
    void test_callRegisterService_when_success() throws Exception {
        MemberRequest.RegisterDto dto =
                new MemberRequest.RegisterDto("username", "password", "username@email.com");
        given(memberService.register(any(MemberRequest.RegisterDto.class))).willReturn(true);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/validate/email"))
                .andExpect(flash().attribute("message", REGISTER_SUCCESS_MESSAGE));

        verify(memberService).register(any(MemberRequest.RegisterDto.class));
    }
    
    /**
     * 회원가입 서비스 호출 메소드의 실패 케이스를 테스트한다.<br>
     * {@link MemberService#register(MemberRequest.RegisterDto)}에서 false를 반환할 때,
     * 실패 메시지와 함께 회원가입 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("회원가입 서비스 호출 테스트 (실패)")
    @WithAnonymousUser
    @Test
    void test_callRegisterService_when_failure() throws Exception {
        MemberRequest.RegisterDto dto =
                new MemberRequest.RegisterDto("username", "password", "username@email.com");
        given(memberService.register(any(MemberRequest.RegisterDto.class))).willReturn(false);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/register"))
                .andExpect(flash().attribute("message", REGISTER_FAILURE_MESSAGE));

        verify(memberService).register(any(MemberRequest.RegisterDto.class));
    }

    /**
     * 비밀번호 변경 서비스를 호출하는 메소드의 성공 케이스를 테스트한다.<br>
     * {@link MemberService#changePassword(MemberRequest.ChangePasswordDto)}에서 true를 반환할 때,
     * 성공 메시지와 함께 로그아웃을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("비밀번호 수정 서비스 호출 테스트 (성공)")
    @WithMockUser
    @Test
    void test_callChangePasswordService_when_success() throws Exception {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "original", "change");
        given(memberService.changePassword(any(MemberRequest.ChangePasswordDto.class))).willReturn(true);

        mockMvc.perform(post("/member/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/logout"))
                .andExpect(flash().attribute("message", CHANGE_PASSWORD_SUCCESS_MESSAGE));

        verify(memberService).changePassword(any(MemberRequest.ChangePasswordDto.class));
    }

    /**
     * 비밀번호 변경 서비스를 호출하는 메소드의 실패 케이스를 테스트한다.<br>
     * {@link MemberService#changePassword(MemberRequest.ChangePasswordDto)}에서 false를 반환할 때,
     * 실패 메시지와 함께 비밀번호 변경 페이지로 리다이렉션을 수행한다.
     * @throws Exception
     *      {@link MockMvc#perform(RequestBuilder)} 수행 중에 발생할 수 있는 예외.
     */
    @DisplayName("비밀번호 수정 서비스 호출 테스트 (실패)")
    @WithMockUser
    @Test
    void test_callChangePasswordService_when_failure() throws Exception {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "original", "change");
        given(memberService.changePassword(any(MemberRequest.ChangePasswordDto.class))).willReturn(false);

        mockMvc.perform(post("/member/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/change/password"))
                .andExpect(flash().attribute("message", CHANGE_PASSWORD_FAILURE_MESSAGE));

        verify(memberService).changePassword(any(MemberRequest.ChangePasswordDto.class));
    }

}
