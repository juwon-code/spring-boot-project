package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 회원명과 일치하는 회원을 조회한다.
     * @param username
     *      회원명.
     * @return
     *      회원 Optional 객체.
     */
    Optional<Member> findMemberByUsername(String username);

    /**
     * 이메일과 일치하는 회원을 조회한다.
     * @param email
     *      이메일.
     * @return
     *      회원 Optional 객체.
     */
    Optional<Member> findMemberByEmail(String email);

    /**
     * 회원명, 비밀번호가 일치하는 회원을 조회한다.
     * @param username
     *      회원명.
     * @param password
     *      비밀번호.
     * @return
     *      회원 Optional 객체.
     */
    Optional<Member> findMemberByUsernameAndPassword(String username, String password);

    /**
     * 회원명, 비밀번호가 일치하는 회원을 삭제한다.
     * @param username
     *      회원명.
     * @param password
     *      비밀번호.
     * @return
     *      삭제한 데이터 개수.
     */
    Long deleteMemberByUsernameAndPassword(String username, String password);

    /**
     * 회원명이 일치하는 회원의 존재 여부를 조회한다.
     * @param username
     *      회원명.
     * @return
     *      회원 존재 여부.
     */
    boolean existsMemberByUsername(String username);

    /**
     * 이메일이 일치하는 회원의 존재 여부를 조회한다.
     * @param email
     *      이메일.
     * @return
     *      회원 존재 여부.
     */
    boolean existsMemberByEmail(String email);

    /**
     * 회원명, 이메일이 일치하는 회원의 존재 여부를 조회한다.
     * @param username
     *      회원명.
     * @param email
     *      이메일.
     * @return
     *      회원 존재 여부.
     */
    boolean existsMemberByUsernameAndEmail(String username, String email);
}
