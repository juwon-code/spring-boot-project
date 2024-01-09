package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByUsernameAndPassword(String username, String password);
    Long deleteMemberByUsernameAndPassword(String username, String password);
    boolean existsMemberByUsername(String username);
    boolean existsMemberByEmail(String email);
}
