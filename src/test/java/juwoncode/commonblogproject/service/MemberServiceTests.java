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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}
