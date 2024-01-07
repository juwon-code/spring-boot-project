package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.request.MemberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean register(MemberRequest.RegisterDto dto) {
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

    @Override
    public boolean changePassword(MemberRequest.ChangePasswordDto dto) {
        try {
            Member member = memberRepository.findMemberByUsernameAndPassword(dto.getUsername(), dto.getOldPassword())
                    .orElseThrow(NoSuchElementException::new);
            member.setPassword(dto.getNewPassword());
            memberRepository.save(member);
            logger.info("Successfully change Member password");
            return true;
        } catch (IllegalArgumentException e) {
            logger.info("Cannot change password with invalid param");
            return false;
        }
    }

    @Override
    public boolean withdraw(MemberRequest.WithdrawDto dto) {
        Long deletedCount = memberRepository.deleteMemberByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        if (deletedCount != 1) {
            logger.info("Cannot delete Member: {}", dto.getUsername());
            return false;
        }

        logger.info("Successfully delete Member: {}", dto.getUsername());
        return true;
    }

    @Override
    public boolean checkUsername(String username) {
        boolean result = memberRepository.existsMemberByUsername(username);

        if (result) {
            logger.info("This username already used: {}", username);
            return false;
        }

        logger.info("This username can be used: {}", username);
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        boolean result = memberRepository.existsMemberByEmail(email);

        if (result) {
            logger.info("This email already used: {}", email);
            return false;
        }

        logger.info("This email can be used: {}", email);
        return true;
    }
}
