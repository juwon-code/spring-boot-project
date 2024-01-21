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

    @DisplayName("메일 전송 서비스 테스트 (실패, 회원 존재하지 않음)")
    @Test
    void test_sendVerifyMail_when_failure() {
        SendDto dto = SendDto.builder()
                .email("username@email.com")
                .type("REGISTER")
                .build();

        Member mockMember = mock(Member.class);
        Email mockEmail = mock(Email.class);
        String fakeHost = "http://localhost:8080/";

        when(memberService.getMemberByEmail(anyString())).thenThrow(NoSuchDataException.class);

        assertThatThrownBy(() -> emailService.sendVerifyMail(dto))
                .isInstanceOf(NoSuchDataException.class);
    }

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
