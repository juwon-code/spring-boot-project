package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {
    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberServiceImpl memberService;

    @DisplayName("회원가입 서비스 테스트 (성공)")
    @Test
    void test_register_when_success() {
        MemberRequest.RegisterDto dto = mock(MemberRequest.RegisterDto.class);
        Member member = mock(Member.class);

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        boolean result = memberService.register(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("회원가입 서비스 테스트 (실패)")
    @Test
    void test_register_when_failure() {
        MemberRequest.RegisterDto dto = mock(MemberRequest.RegisterDto.class);

        when(memberRepository.save(any(Member.class))).thenThrow(IllegalArgumentException.class);

        boolean result = memberService.register(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호 변경 서비스 테스트 (성공)")
    @Test
    void test_changePassword_when_success() {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "oldpass", "newpass");
        Member member = Member.builder()
                .username("username")
                .password("$2a$12$XuFPe78trFrr9DD3r8R90uPKmerD8g0JcsmyxN6Bufk5t4Z0cEp0G")
                .build();

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));
        when(memberService.checkPasswordMatched(anyString(), anyString())).thenReturn(true);

        boolean result = memberService.changePassword(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("비밀번호 변경 서비스 테스트 (실패, 존재하지 않는 회원)")
    @Test
    void test_changePassword_when_memberNotExists() {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "oldpass", "newpass");

        when(memberRepository.findMemberByUsername(anyString())).thenThrow(NoSuchDataException.class);

        boolean result = memberService.changePassword(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호 변경 서비스 테스트 (실패, 비밀번호가 다름)")
    @Test
    void test_changePassword_when_passwordNotMatch() {
        MemberRequest.ChangePasswordDto dto =
                new MemberRequest.ChangePasswordDto("username", "oldpass", "newpass");
        Member member = mock(Member.class);

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));

        boolean result = memberService.changePassword(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("회원탈퇴 서비스 테스트 (성공)")
    @Test
    void test_withdraw_when_success() {
        MemberRequest.WithdrawDto dto = new MemberRequest.WithdrawDto("username", "password");
        Member member = Member.builder()
                .username("username")
                .password("$2a$12$XuFPe78trFrr9DD3r8R90uPKmerD8g0JcsmyxN6Bufk5t4Z0cEp0G")
                .build();

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));
        when(memberService.checkPasswordMatched(anyString(), anyString())).thenReturn(true);
        when(memberRepository.deleteMemberByUsernameAndPassword(anyString(), anyString())).thenReturn(1L);

        boolean result = memberService.withdraw(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("회원탈퇴 서비스 테스트 (실패, 존재하지 않는 회원)")
    @Test
    void test_withdraw_when_memberNotExists() {
        MemberRequest.WithdrawDto dto = new MemberRequest.WithdrawDto("username", "password");

        when(memberRepository.findMemberByUsername(anyString())).thenThrow(NoSuchDataException.class);

        boolean result = memberService.withdraw(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("회원탈퇴 서비스 테스트 (실패, 비밀번호가 다름)")
    @Test
    void test_withdraw_when_passwordNotMatch() {
        MemberRequest.WithdrawDto dto = new MemberRequest.WithdrawDto("username", "password");
        Member member = mock(Member.class);

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));

        boolean result = memberService.withdraw(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("아이디 중복 검사 서비스 테스트 (성공)")
    @Test
    void test_checkUsername_when_success() {
        String username = "username";

        when(memberRepository.existsMemberByUsername(anyString())).thenReturn(false);

        boolean result = memberService.checkUsernameDuplicated(username);
        assertThat(result).isTrue();
    }

    @DisplayName("아이디 중복 검사 서비스 테스트 (실패)")
    @Test
    void test_checkUsername_when_failure() {
        String username = "username";

        when(memberRepository.existsMemberByUsername(anyString())).thenReturn(true);

        boolean result = memberService.checkUsernameDuplicated(username);
        assertThat(result).isFalse();
    }

    @DisplayName("이메일 중복 검사 서비스 테스트 (성공)")
    @Test
    void test_checkEmail_when_success() {
        String email = "aaa@email.com";

        when(memberRepository.existsMemberByEmail(anyString())).thenReturn(false);

        boolean result = memberService.checkEmailDuplicated(email);
        assertThat(result).isTrue();
    }

    @DisplayName("이메일 중복 검사 서비스 테스트 (실패)")
    @Test
    void test_checkEmail_when_failure() {
        String email = "aaa@email.com";

        when(memberRepository.existsMemberByEmail(anyString())).thenReturn(true);

        boolean result = memberService.checkEmailDuplicated(email);
        assertThat(result).isFalse();
    }
}
