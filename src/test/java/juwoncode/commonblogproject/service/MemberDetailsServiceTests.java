package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.vo.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberDetailsServiceTests {
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberDetailsService memberDetailsService;

    @DisplayName("회원조회 서비스 테스트 (성공)")
    @Test
    public void test_loadUserByUsername_when_success() {
        String username = "username";
        Member member = Member.builder()
                .username("username")
                .password("password")
                .email("username@email.com")
                .role(Role.USER)
                .build();

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));

        UserDetails result = memberDetailsService.loadUserByUsername(username);
        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getAuthorities()).isNotEmpty();
    }

    @DisplayName("회원조회 서비스 테스트 (실패)")
    @Test
    public void test_loadUserByUsername_when_failure() {
        when(memberRepository.findMemberByUsername(anyString())).thenThrow(UsernameNotFoundException.class);

        assertThatThrownBy(() -> memberDetailsService.loadUserByUsername(anyString()))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
