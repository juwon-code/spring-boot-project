package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberRepositoryTests {
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .username("username")
                .password("password")
                .email("username@email.com")
                .build();

        memberRepository.save(member);
    }

    @DisplayName("회원조회 쿼리 테스트 : 아이디, 비밀번호 (성공)")
    @Test
    void test_findMemberByUsernameAndPassword_when_success() {
        Member result = memberRepository.findMemberByUsernameAndPassword("username", "password")
                .orElseThrow(IllegalArgumentException::new);

        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getEmail()).isEqualTo("username@email.com");

    }

    @DisplayName("회원조회 쿼리 테스트 : 아이디, 비밀번호 (실패)")
    @Test
    void test_findMemberByUsernameAndPassword_when_failure() {
        assertThatThrownBy(() -> {
            Member result = memberRepository.findMemberByUsernameAndPassword("username1", "password")
                    .orElseThrow(IllegalArgumentException::new);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원삭제 쿼리 테스트 : 아이디, 비밀번호 (성공)")
    @Test
    void test_deleteMemberByUsernameAndPassword_when_success() {
        Long result = memberRepository.deleteMemberByUsernameAndPassword("username", "password");

        assertThat(result).isEqualTo(1);
    }

    @DisplayName("회원삭제 쿼리 테스트 : 아이디, 비밀번호 (실패)")
    @Test
    void test_deleteMemberByUsernameAndPassword_when_failure() {
        Long result = memberRepository.deleteMemberByUsernameAndPassword("username1", "password");

        assertThat(result).isZero();
    }

    @DisplayName("회원존재 쿼리 테스트 : 아이디 (존재함)")
    @Test
    void test_existsMemberByUsername_when_exists() {
        boolean result = memberRepository.existsMemberByUsername("username");

        assertThat(result).isTrue();
    }

    @DisplayName("회원존재 쿼리 테스트 : 아이디 (존재하지않음)")
    @Test
    void test_existsMemberByUsername_when_notExists() {
        boolean result = memberRepository.existsMemberByUsername("usernameValid");

        assertThat(result).isFalse();
    }
}
