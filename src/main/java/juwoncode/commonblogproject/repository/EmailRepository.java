package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.vo.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findEmailByMember_EmailAndTypeAndExpired(String email, EmailType type, boolean expired);

    Optional<Email> findEmailByCodeAndType(String code, EmailType type);
}
