package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.EmailRequest;
import juwoncode.commonblogproject.dto.MemberRequest;

import static juwoncode.commonblogproject.dto.EmailRequest.*;

public interface MemberService {
    /**
     * 회원을 데이터베이스에 저장하고 성공 여부를 반환한다.<br>
     * 회원명, 이메일, 비밀번호를 파라미터로 중복 여부를 검사하고,
     * 회원을 저장한 뒤 성공 여부를 반환한다.
     * @param dto
     *      {@link juwoncode.commonblogproject.dto.MemberRequest.RegisterDto} 객체.
     * @return
     *      회원저장 성공 여부.
     */
    boolean register(MemberRequest.RegisterDto dto);

    /**
     * 데이터베이스의 회원을 삭제하고 성공 여부를 반환한다.<br>
     * 회원명, 비밀번호를 파라미터로 회원이 존재하는지, 비밀번호가 일치하는지 검사하고,
     * 회원을 삭제한 뒤 성공 여부를 반환한다.
     * @param dto
     *      {@link juwoncode.commonblogproject.dto.MemberRequest.WithdrawDto} 객체.
     * @return
     *      회원삭제 성공 여부.
     */
    boolean withdraw(MemberRequest.WithdrawDto dto);

    /**
     * 회원의 비밀번호를 업데이트 한다.<br>
     * 회원명, 비밀번호, 새 비밀번호를 파라미터로 회원이 존재하는지, 비밀번호가 일치하는지 검사하고, 
     * 새 비밀번호로 업데이트한 뒤 성공 여부를 반환한다.
     * @param dto
     *      {@link juwoncode.commonblogproject.dto.MemberRequest.ChangePasswordDto} 객체.
     * @return
     *      비밀번호 업데이트 성공 여부.
     */
    boolean changePassword(MemberRequest.ChangePasswordDto dto);

    /**
     * 비밀번호가 일치하는지 검사한다.<br>
     * 검사할 비밀번호, 올바른 비밀번호를 파라미터로 일치여부를 검사하고 결과를 반환한다.
     * @param rawPassword
     *      검사할 비밀번호.
     * @param encodedPassword
     *      올바른 비밀번호.
     * @return
     *      비밀번호 일치 여부.
     */
    boolean checkPasswordMatched(String rawPassword, String encodedPassword);

    /**
     * 이메일을 파라미터로 일치하는 회원을 조회한다.
     * @param email
     *      회원 메일 주소.
     * @return
     *      조회한 회원 인스턴스.
     */
    Member getMemberByEmail(String email);

    /**
     * 회원을 활성화한다.
     * 회원 객체를 파라미터로 활성화하고 데이터베이스에 저장한다.
     * @param dto
     *      {@link ExpirationDto} 객체.
     */
    boolean setMemberEnabled(EmailRequest.ExpirationDto dto);

    /**
     * 회원명의 중복 여부를 반환한다.<br>
     * 회원명이 일치하는 회원이 데이터베이스에 존재하는지 검사하고 결과를 반환한다.
     * @param username
     *      회원명.
     * @return
     *      중복 여부.
     */
    boolean checkUsernameDuplicated(String username);

    /**
     * 회원 메일 주소의 중복 여부를 반환한다.<br>
     * 이메일이 일치하는 회원이 데이터베이스에 존재하는지 검사하고 결과를 반환한다.
     * @param email
     *      회원 메일 주소.
     * @return
     *      중복 여부.
     */
    boolean checkEmailDuplicated(String email);
}
