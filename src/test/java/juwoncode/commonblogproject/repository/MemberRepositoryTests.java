package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.vo.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberRepositoryTests {
    @Autowired
    MemberRepository memberRepository;

    /**
     * 각 테스트를 수행하기 전에 임시 회원을 저장한다.
     */
    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .username("username")
                .password("password")
                .email("username@email.com")
                .role(RoleType.USER)
                .build();

        memberRepository.save(member);
    }

    /**
     * 회원명, 비밀번호로 회원을 조회하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("회원조회 쿼리 테스트 : 아이디, 비밀번호 (성공)")
    @Test
    void test_findMemberByUsernameAndPassword_when_success() {
        Member result = memberRepository.findMemberByUsernameAndPassword("username", "password")
                .orElseThrow(IllegalArgumentException::new);

        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getEmail()).isEqualTo("username@email.com");
        assertThat(result.getRole()).isEqualTo(RoleType.USER);
        assertThat(result.isEnabled()).isFalse();
    }

    /**
     * 회원명, 비밀번호로 회원을 조회하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("회원조회 쿼리 테스트 : 아이디, 비밀번호 (실패)")
    @Test
    void test_findMemberByUsernameAndPassword_when_failure() {
        assertThatThrownBy(() -> {
            memberRepository.findMemberByUsernameAndPassword("username1", "password")
                    .orElseThrow(IllegalArgumentException::new);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * 회원명, 비밀번호와 일치하는 회원을 삭제하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("회원삭제 쿼리 테스트 : 아이디, 비밀번호 (성공)")
    @Test
    void test_deleteMemberByUsernameAndPassword_when_success() {
        Long result = memberRepository.deleteMemberByUsernameAndPassword("username", "password");

        assertThat(result).isEqualTo(1);
    }

    /**
     * 회원명, 비밀번호와 일치하는 회원을 삭제하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("회원삭제 쿼리 테스트 : 아이디, 비밀번호 (실패)")
    @Test
    void test_deleteMemberByUsernameAndPassword_when_failure() {
        Long result = memberRepository.deleteMemberByUsernameAndPassword("username1", "password");

        assertThat(result).isZero();
    }

    /**
     * 회원명과 일치하는 데이터의 존재 여부를 반환하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 아이디 (존재함)")
    @Test
    void test_existsMemberByUsername_when_exists() {
        boolean result = memberRepository.existsMemberByUsername("username");

        assertThat(result).isTrue();
    }

    /**
     * 회원명과 일치하는 데이터의 존재 여부를 반환하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 아이디 (존재하지 않음)")
    @Test
    void test_existsMemberByUsername_when_notExists() {
        boolean result = memberRepository.existsMemberByUsername("usernameValid");

        assertThat(result).isFalse();
    }

    /**
     * 회원 메일주소와 일치하는 데이터의 존재 여부를 반환하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 이메일 (존재함)")
    @Test
    void test_existsMemberByEmail_when_exists() {
        boolean result = memberRepository.existsMemberByEmail("username@email.com");

        assertThat(result).isTrue();
    }

    /**
     * 회원 메일주소와 일치하는 데이터의 존재 여부를 반환하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 이메일 (존재하지 않음)")
    @Test
    void test_existsMemberByEmail_when_notExists() {
        boolean result = memberRepository.existsMemberByEmail("username1@email.com");

        assertThat(result).isFalse();
    }

    /**
     * 회원명, 메일주소와 일치하는 데이터의 존재 여부를 반환하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 회원, 이메일 (존재함)")
    @Test
    void test_existsMemberByUsernameAndEmail_when_exists() {
        boolean result = memberRepository.existsMemberByUsernameAndEmail("username", "username@email.com");

        assertThat(result).isTrue();
    }

    /**
     * 회원명, 메일주소와 일치하는 데이터의 존재 여부를 반환하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("회원 중복 검사 쿼리 테스트 : 회원, 이메일 (존재하지 않음)")
    @Test
    void test_existsMemberByUsernameAndEmail_when_notExists() {
        boolean result = memberRepository.existsMemberByUsernameAndEmail("username1", "username1@email.com");

        assertThat(result).isFalse();
    }
}
