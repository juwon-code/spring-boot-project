package juwoncode.commonblogproject.service;

import jakarta.transaction.Transactional;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.MemberRepository;
import juwoncode.commonblogproject.dto.MemberRequest;
import juwoncode.commonblogproject.vo.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static juwoncode.commonblogproject.vo.LoggerMessage.*;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public boolean register(MemberRequest.RegisterDto dto) {
        String username = dto.getUsername();
        String email = dto.getEmail();
        String password = dto.getPassword();

        try {
            if (checkMemberRegistered(username, email)) {
                throw new IllegalArgumentException(REGISTER_SERVICE_FAILURE_LOG);
            }

            Member member = Member.builder()
                    .username(username)
                    .password(encryptPassword(password))
                    .email(email)
                    .role(RoleType.USER)
                    .build();

            memberRepository.save(member);
            logger.info(REGISTER_SERVICE_SUCCESS_LOG, username);
            return true;
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage(), username);
            return false;
        }
    }

    private boolean checkMemberRegistered(String username, String password) {
        return memberRepository.existsMemberByUsernameAndEmail(username, password);
    }

    @Override
    @Transactional
    public boolean changePassword(MemberRequest.ChangePasswordDto dto) {
        String username = dto.getUsername();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();

        try {
            Member member = memberRepository.findMemberByUsername(username)
                    .orElseThrow(() -> new NoSuchDataException(CHANGE_PASSWORD_SERVICE_EMPTY_LOG));
            String correctPassword = member.getPassword();

            if (!checkPasswordMatched(oldPassword, correctPassword)) {
                throw new IllegalArgumentException(CHANGE_PASSWORD_SERVICE_WRONG_LOG);
            }

            member.setPassword(encryptPassword(newPassword));
            memberRepository.save(member);
            logger.info(CHANGE_PASSWORD_SERVICE_SUCCESS_LOG, username);
            return true;
        } catch (NoSuchDataException | IllegalArgumentException e) {
            logger.info(e.getMessage(), username);
            return false;
        }
    }

    private String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    @Transactional
    public boolean withdraw(MemberRequest.WithdrawDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        try {
            Member member = memberRepository.findMemberByUsername(username)
                    .orElseThrow(() -> new NoSuchDataException(WITHDRAW_SERVICE_EMPTY_LOG));
            String correctPassword = member.getPassword();

            if (!checkPasswordMatched(password, correctPassword)) {
                throw new IllegalArgumentException(WITHDRAW_SERVICE_WRONG_LOG);
            }

            memberRepository.deleteMemberByUsernameAndPassword(username, password);
            logger.info(WITHDRAW_SERVICE_SUCCESS_LOG, username);
            return true;
        } catch (NoSuchDataException | IllegalArgumentException e) {
            logger.info(e.getMessage(), username);
            return false;
        }
    }

    @Override
    public boolean checkPasswordMatched(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new NoSuchDataException(FIND_MEMBER_WITH_EMAIL_FAILURE_LOG));
    }

    @Override
    public void setMemberEnabled(Member member) {
        member.setEnabled(true);
        memberRepository.save(member);
    }

    @Override
    public boolean checkUsernameDuplicated(String username) {
        return checkDataDuplicated(username, memberRepository::existsMemberByUsername,
                CHECK_USERNAME_SERVICE_SUCCESS_LOG, CHECK_USERNAME_SERVICE_FAILURE_LOG);
    }

    @Override
    public boolean checkEmailDuplicated(String email) {
        return checkDataDuplicated(email, memberRepository::existsMemberByEmail,
                CHECK_EMAIL_SERVICE_SUCCESS_LOG, CHECK_EMAIL_SERVICE_FAILURE_LOG);
    }

    private boolean checkDataDuplicated(String data, Function<String, Boolean> checkFunction, String successLog, String failureLog) {
        boolean result = checkFunction.apply(data);

        if (result) {
            logger.info(successLog, data);
            return false;
        }

        logger.info(failureLog, data);
        return true;
    }
}
