package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.vo.EmailType;
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
public class EmailRepositoryTests {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmailRepository emailRepository;

    /**
     * 각 테스트를 수행하기 전에 임시 회원 및 메일을 저장한다.
     */
    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .username("username")
                .password("$2a$12$NuRqMTFcVdfPdNU2/VQQcOqZXuU.os/cZIEE/P2xvaiSomzRppg.q") // Password12345!
                .email("username@email.com")
                .role(RoleType.USER)
                .build();
        member = memberRepository.save(member);

        Email email = Email.builder()
                .type(EmailType.REGISTER)
                .code("aAbBcCdDeEfFgGhH")
                .member(member)
                .build();
        emailRepository.save(email);
    }

    /**
     * 회원 메일주소, 메일 타입으로 인증메일을 조회하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("인증메일조회 쿼리 테스트 : 이메일, 타입, 만료여부 (성공)")
    @Test
    void test_findEmailByMember_EmailAndTypeAndExpired_when_success() {
        Email result = emailRepository.findEmailByMember_EmailAndTypeAndExpired("username@email.com"
                , EmailType.valueOf("REGISTER"), false).orElseThrow(NoSuchDataException::new);

        assertThat(result.getCode()).isEqualTo("aAbBcCdDeEfFgGhH");
        assertThat(result.getType()).isEqualTo(EmailType.valueOf("REGISTER"));
        assertThat(result.isExpired()).isFalse();
        assertThat(result.getMember()).isNotNull();
    }

    /**
     * 회원 메일주소, 메일 타입으로 인증메일을 조회하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("인증메일조회 쿼리 테스트 : 이메일, 타입, 만료여부 (실패)")
    @Test
    void test_findEmailByMember_EmailAndTypeAndExpired_when_failure() {
        assertThatThrownBy(() -> {
            emailRepository.findEmailByMember_EmailAndTypeAndExpired("username1@email.com"
                    , EmailType.valueOf("REGISTER"), true).orElseThrow(NoSuchDataException::new);
        }).isInstanceOf(NoSuchDataException.class);
    }

    /**
     * 메일 코드, 메일 타입으로 인증메일을 조회하는 메소드의 성공 케이스를 테스트한다.
     */
    @DisplayName("인증메일조회 쿼리 테스트 : 코드, 타입 (성공)")
    @Test
    void test_findEmailByCodeAndType_when_success() {
        Email result = emailRepository.findEmailByCodeAndType("aAbBcCdDeEfFgGhH", EmailType.valueOf("REGISTER"))
                .orElseThrow(NoSuchDataException::new);

        assertThat(result.getCode()).isEqualTo("aAbBcCdDeEfFgGhH");
        assertThat(result.getType()).isEqualTo(EmailType.valueOf("REGISTER"));
        assertThat(result.isExpired()).isFalse();
        assertThat(result.getMember()).isNotNull();
    }

    /**
     * 메일 코드, 메일 타입으로 인증메일을 조회하는 메소드의 실패 케이스를 테스트한다.
     */
    @DisplayName("인증메일조회 쿼리 테스트 : 코드, 타입 (실패)")
    @Test
    void test_findEmailByCodeAndType_when_failure() {
        assertThatThrownBy(() -> {
            emailRepository.findEmailByCodeAndType("BBBBBBBBBBBB", EmailType.valueOf("REGISTER"))
                    .orElseThrow(NoSuchDataException::new);
        }).isInstanceOf(NoSuchDataException.class);
    }
}
