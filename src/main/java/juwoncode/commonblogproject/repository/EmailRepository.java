package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.vo.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    /**
     * 회원 이메일, 메일 타입 및 만료 여부가 일치하는 인증 메일을 조회한다.
     * @param email
     *      회원 이메일.
     * @param type
     *      메일 타입.
     * @param expired
     *      메일 만료 여부.
     * @return
     *      메일 {@link Optional} 객체.
     */
    Optional<Email> findEmailByMember_EmailAndTypeAndExpired(String email, EmailType type, boolean expired);

    /**
     * 메일 코드, 타입이 일치하는 인증 메일을 조회한다.
     * @param code
     *      메일 코드.
     * @param type
     *      메일 타입.
     * @return
     *      메일 {@link Optional} 객체.
     */
    Optional<Email> findEmailByCodeAndType(String code, EmailType type);
}
