package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.vo.RoleType;
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

    /**
     * 회원조회 서비스 메소드의 성공 케이스를 테스트한다.<br>
     * 회원명과 일치하는 회원이 존재할 경우 MemberDetails 객체를 반환한다.
     * @see
     *      MemberRepository#findMemberByUsername(String)
     * @see
     *      juwoncode.commonblogproject.dto.MemberDetails
     */
    @DisplayName("회원조회 서비스 테스트 (성공)")
    @Test
    public void test_loadUserByUsername_when_success() {
        String username = "username";
        Member member = Member.builder()
                .username("username")
                .password("password")
                .email("username@email.com")
                .role(RoleType.USER)
                .build();

        when(memberRepository.findMemberByUsername(anyString())).thenReturn(Optional.of(member));

        UserDetails result = memberDetailsService.loadUserByUsername(username);
        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getAuthorities()).isNotEmpty();
    }

    /**
     * 회원조회 서비스 메소드의 실패 케이스를 테스트한다.<br>
     * 회원명과 일치하는 회원이 존재하지 않을 경우 {@link UsernameNotFoundException} 예외가 발생한다.
     * @see
     *      MemberRepository#findMemberByUsername(String)
     */
    @DisplayName("회원조회 서비스 테스트 (실패)")
    @Test
    public void test_loadUserByUsername_when_failure() {
        when(memberRepository.findMemberByUsername(anyString())).thenThrow(UsernameNotFoundException.class);

        assertThatThrownBy(() -> memberDetailsService.loadUserByUsername(anyString()))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
