package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.MemberDto;
import juwoncode.commonblogproject.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberServiceImpl memberService;

    @DisplayName("회원가입 서비스 테스트 (성공)")
    @Test
    void test_register_when_success() {
        MemberDto.RequestDto dto = mock(MemberDto.RequestDto.class);
        Member member = mock(Member.class);

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        boolean result = memberService.register(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("회원가입 서비스 테스트 (실패)")
    @Test
    void test_register_when_failure() {
        MemberDto.RequestDto dto = mock(MemberDto.RequestDto.class);

        when(memberRepository.save(any(Member.class))).thenThrow(IllegalArgumentException.class);

        boolean result = memberService.register(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호 변경 서비스 테스트 (성공)")
    @Test
    void test_changePassword_when_success() {
        MemberDto.ChangePasswordRequestDto dto =
                new MemberDto.ChangePasswordRequestDto("username", "oldpass", "newpass");
        Member member = mock(Member.class);

        when(memberRepository.findMemberByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(member));

        boolean result = memberService.changePassword(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("비밀번호 변경 서비스 테스트 (실패)")
    @Test
    void test_changePassword_when_failure() {
        MemberDto.ChangePasswordRequestDto dto =
                new MemberDto.ChangePasswordRequestDto("username", "oldpass", "newpass");

        when(memberRepository.findMemberByUsernameAndPassword(anyString(), anyString()))
                .thenThrow(IllegalArgumentException.class);

        boolean result = memberService.changePassword(dto);
        assertThat(result).isFalse();
    }

    @DisplayName("회원탈퇴 서비스 테스트 (성공)")
    @Test
    void test_withdraw_when_success() {
        MemberDto.WithdrawRequestDto dto = new MemberDto.WithdrawRequestDto("username", "password");

        when(memberRepository.deleteMemberByUsernameAndPassword(anyString(), anyString())).thenReturn(1L);

        boolean result = memberService.withdraw(dto);
        assertThat(result).isTrue();
    }

    @DisplayName("회원탈퇴 서비스 테스트 (실패)")
    @Test
    void test_withdraw_when_failure() {
        MemberDto.WithdrawRequestDto dto = new MemberDto.WithdrawRequestDto("username", "password");

        when(memberRepository.deleteMemberByUsernameAndPassword(anyString(), anyString())).thenReturn(0L);

        boolean result = memberService.withdraw(dto);
        assertThat(result).isFalse();
    }
}
