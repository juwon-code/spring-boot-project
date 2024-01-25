package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.config.LoggerProvider;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.MemberDetails;
import juwoncode.commonblogproject.repository.MemberRepository;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static juwoncode.commonblogproject.vo.LoggerMessage.*;

@Service
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerProvider.getLogger(this.getClass());

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원명과 일치하는 회원을 조회한다.
     * 회원명과 일치하는 {@link Member} 객체를 조회하고, 존재할 경우 {@link MemberDetails} 객체로 변환하여 반환한다.
     * @param username 
     *      회원명.
     * @return
     *      조회한 회원 객체.
     * @exception UsernameNotFoundException
     *      회원이 존재하지 않을 경우 발생하는 예외.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            Member member = memberRepository.findMemberByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(FIND_MEMBER_WITH_USERNAME_FAILURE_LOG + username));

            logger.info(FIND_MEMBER_WITH_USERNAME_SUCCESS_LOG + username);
            return MemberDetails.builder()
                    .username(username)
                    .password(member.getPassword())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .enabled(member.isEnabled())
                    .build();
        } catch (UsernameNotFoundException e) {
            logger.info(e.getMessage());
            throw e;
        }
    }
}
