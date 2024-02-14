package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.config.DynamicHostProvider;
import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.EmailRequest;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.EmailRepository;
import juwoncode.commonblogproject.vo.EmailType;
import juwoncode.commonblogproject.vo.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static juwoncode.commonblogproject.dto.EmailRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {
    @Mock
    EmailRepository emailRepository;

    @Mock
    MemberService memberService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    DynamicHostProvider dynamicHostProvider;

    @InjectMocks
    EmailServiceImpl emailService;

    /**
     * 인증메일 전송 서비스 메소드의 성공 케이스를 테스트한다.<br>
     * 가짜 회원, 메일, 주소를 파라미터로 메소드가 흐름대로 작동하는지 확인한다.
     * @see
     *      MemberService#getMemberByEmail(String)
     * @see
     *      EmailRepository#findEmailByMember_EmailAndTypeAndExpired(String, EmailType, boolean)
     * @see
     *      EmailRepository#save(Object)
     * @see
     *      DynamicHostProvider#getHost()
     */
    @DisplayName("메일 전송 서비스 테스트 (성공)")
    @Test
    void test_sendVerifyMail_when_success() {
        SendDto dto = SendDto.builder()
                .email("username@email.com")
                .type("REGISTER")
                .build();

        Member mockMember = mock(Member.class);
        Email mockEmail = mock(Email.class);
        String fakeHost = "http://localhost:8080/";

        when(memberService.getMemberByEmail(anyString())).thenReturn(mockMember);
        when(emailRepository.findEmailByMember_EmailAndTypeAndExpired(anyString(), any(EmailType.class), anyBoolean()))
                .thenReturn(Optional.of(mockEmail));
        when(emailRepository.save(any(Email.class))).thenReturn(mockEmail);
        when(dynamicHostProvider.getHost()).thenReturn(fakeHost);
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendVerifyMail(dto);
    }

    /**
     * 인증메일 전송 서비스 메소드의 실패 케이스를 테스트한다.<br>
     * 회원 메일 주소, 메일 타입과 일차하는 인증 메일이 없을 경우, {@link NoSuchDataException} 예외가 발생한다.
     * @see
     *      MemberService#getMemberByEmail(String)
     */
    @DisplayName("메일 전송 서비스 테스트 (실패, 회원 존재하지 않음)")
    @Test
    void test_sendVerifyMail_when_failure() {
        SendDto dto = SendDto.builder()
                .email("username@email.com")
                .type("REGISTER")
                .build();

        when(memberService.getMemberByEmail(anyString())).thenThrow(NoSuchDataException.class);

        assertThatThrownBy(() -> emailService.sendVerifyMail(dto))
                .isInstanceOf(NoSuchDataException.class);
    }

    /**
     * 인증메일 확인 서비스 메소드의 성공 케이스를 테스트한다.<br>
     * 메일 코드, 타입과 일치하는 인증메일이 만료되지 않았을 경우, true를 반환한다.
     * @see
     *      EmailRepository#findEmailByCodeAndType(String, EmailType)
     * @see
     *      EmailRepository#save(Object) 
     * @see
     *      MemberService#setMemberEnabled(Member)
     */
    @DisplayName("메일 확인 서비스 테스트 (성공)")
    @Test
    void test_checkVerifyMail_when_success() {
        CheckDto dto = CheckDto.builder()
                .type("REGISTER")
                .code("AaBbCcDdFF")
                .build();

        Member fakeMember = Member.builder()
                .username("username")
                .password("$2a$12$NuRqMTFcVdfPdNU2/VQQcOqZXuU.os/cZIEE/P2xvaiSomzRppg.q") // Password12345!
                .email("username@email.com")
                .role(RoleType.USER)
                .build();

        Email fakeEmail = Email.builder()
                .type(EmailType.valueOf("REGISTER"))
                .code("AaBbCcDdFF")
                .member(fakeMember)
                .build();

        when(emailRepository.findEmailByCodeAndType(anyString(), any(EmailType.class))).thenReturn(Optional.of(fakeEmail));
        doNothing().when(memberService).setMemberEnabled(any(Member.class));
        when(emailRepository.save(any(Email.class))).thenReturn(fakeEmail);

        boolean result = emailService.checkVerifyMail(dto);
        assertThat(result).isTrue();
    }

    /**
     * 인증메일 확인 서비스 메소드의 실패 케이스를 테스트한다.<br>
     * 메일 코드, 타입과 일치하는 인증 메일이 존재하지 않을 때, false를 반환한다.
     * @see
     *      EmailRepository#findEmailByCodeAndType(String, EmailType)
     */
    @DisplayName("메일 확인 서비스 테스트 (실패, 존재하지 않는 이메일)")
    @Test
    void test_checkVerifyMail_when_notExists() {
        CheckDto dto = CheckDto.builder()
                .type("REGISTER")
                .code("AaBbCcDdFF")
                .build();

        Member fakeMember = Member.builder()
                .username("username")
                .password("$2a$12$NuRqMTFcVdfPdNU2/VQQcOqZXuU.os/cZIEE/P2xvaiSomzRppg.q") // Password12345!
                .email("username@email.com")
                .role(RoleType.USER)
                .build();

        Email fakeEmail = Email.builder()
                .type(EmailType.valueOf("REGISTER"))
                .code("AaBbCcDdFF")
                .member(fakeMember)
                .build();
        fakeEmail.setExpired(true);

        when(emailRepository.findEmailByCodeAndType(anyString(), any(EmailType.class)))
                .thenThrow(NoSuchDataException.class);

        boolean result = emailService.checkVerifyMail(dto);
        assertThat(result).isFalse();
    }

    /**
     * 인증메일 확인 서비스 메소드의 실패 케이스를 테스트한다.<br>
     * 메일 코드, 타입과 일치하는 인증 메일이 만료된 상태일 경우, false를 반환한다.
     * @see
     *      EmailRepository#findEmailByCodeAndType(String, EmailType)
     */
    @DisplayName("메일 확인 서비스 테스트 (실패, 이미 만료됨)")
    @Test
    void test_checkVerifyMail_when_alreadyExpired() {
        CheckDto dto = CheckDto.builder()
                .type("REGISTER")
                .code("AaBbCcDdFF")
                .build();

        Member fakeMember = Member.builder()
                .username("username")
                .password("$2a$12$NuRqMTFcVdfPdNU2/VQQcOqZXuU.os/cZIEE/P2xvaiSomzRppg.q") // Password12345!
                .email("username@email.com")
                .role(RoleType.USER)
                .build();

        Email fakeEmail = Email.builder()
                .type(EmailType.valueOf("REGISTER"))
                .code("AaBbCcDdFF")
                .member(fakeMember)
                .build();
        fakeEmail.setExpired(true);

        when(emailRepository.findEmailByCodeAndType(anyString(), any(EmailType.class))).thenReturn(Optional.of(fakeEmail));

        boolean result = emailService.checkVerifyMail(dto);
        assertThat(result).isFalse();
    }
}
