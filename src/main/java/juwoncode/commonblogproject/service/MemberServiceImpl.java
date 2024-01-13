package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.vo.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static juwoncode.commonblogproject.vo.LoggerMessage.*;

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
                .role(Role.USER)
                .build();

        try {
            memberRepository.save(member);
            logger.info(REGISTER_SERVICE_SUCCESS_LOG, dto.getUsername());
            return true;
        } catch (IllegalArgumentException e) {
            logger.info(REGISTER_SERVICE_FAILURE_LOG, dto.getUsername());
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
            logger.info(CHANGE_PASSWORD_SERVICE_SUCCESS_LOG, dto.getUsername());
            return true;
        } catch (IllegalArgumentException e) {
            logger.info(CHANGE_PASSWORD_SERVICE_FAILURE_LOG, dto.getUsername());
            return false;
        }
    }

    @Override
    public boolean withdraw(MemberRequest.WithdrawDto dto) {
        Long deletedCount = memberRepository.deleteMemberByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        if (deletedCount != 1) {
            logger.info(WITHDRAW_SERVICE_SUCCESS_LOG, dto.getUsername());
            return false;
        }

        logger.info(WITHDRAW_SERVICE_FAILURE_LOG, dto.getUsername());
        return true;
    }

    @Override
    public boolean checkUsername(String username) {
        boolean result = memberRepository.existsMemberByUsername(username);

        if (result) {
            logger.info(CHECK_USERNAME_SERVICE_SUCCESS_LOG, username);
            return false;
        }

        logger.info(CHECK_USERNAME_SERVICE_FAILURE_LOG, username);
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        boolean result = memberRepository.existsMemberByEmail(email);

        if (result) {
            logger.info(CHECK_EMAIL_SERVICE_SUCCESS_LOG, email);
            return false;
        }

        logger.info(CHECK_EMAIL_SERVICE_FAILURE_LOG, email);
        return true;
    }
}
