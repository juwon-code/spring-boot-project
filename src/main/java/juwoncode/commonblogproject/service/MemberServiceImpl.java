package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.dto.MemberDto;
import juwoncode.commonblogproject.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean register(MemberDto.RequestDto dto) {
        Member member = Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();

        try {
            memberRepository.save(member);
            logger.info("Successfully save new member: {}", dto.getUsername());
            return true;
        } catch (IllegalArgumentException e) {
            logger.info("Cannot save Member with empty params");
            return false;
        }
    }
}
