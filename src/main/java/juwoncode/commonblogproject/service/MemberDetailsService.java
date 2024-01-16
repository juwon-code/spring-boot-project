package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.MemberDetails;
import juwoncode.commonblogproject.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static juwoncode.commonblogproject.vo.LoggerMessage.*;

@Service
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
