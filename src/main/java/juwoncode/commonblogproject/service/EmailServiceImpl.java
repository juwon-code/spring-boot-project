package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.config.DynamicHostProvider;
import juwoncode.commonblogproject.config.LoggerProvider;
import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.EmailRepository;
import juwoncode.commonblogproject.vo.EmailType;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static juwoncode.commonblogproject.dto.EmailRequest.*;
import static juwoncode.commonblogproject.vo.LoggerMessage.*;
import static juwoncode.commonblogproject.vo.MailConstants.*;

@Service
public class EmailServiceImpl implements EmailService {
    private final MemberService memberService;
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final DynamicHostProvider dynamicHostProvider;
    private final Logger logger = LoggerProvider.getLogger(this.getClass());

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender javaMailSender, MemberService memberService
            , DynamicHostProvider dynamicHostProvider) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.memberService = memberService;
        this.dynamicHostProvider = dynamicHostProvider;
    }

    @Override
    public void sendVerifyMail(SendDto dto) {
        String email = dto.getEmail();
        EmailType type = EmailType.valueOf(dto.getType());
        String code = generateRandomCode();
        Member member = memberService.getMemberByEmail(email);

        expirePreviousMail(email, type);

        saveMailDB(code, type, member);

        sendMail(email, code, type);
    }

    private void sendMail(String email, String code, EmailType type) {
        SimpleMailMessage message = makeVerificationMail(email, code, type);
        javaMailSender.send(message);
    }

    @Override
    public boolean checkVerifyMail(CheckDto dto) {
        String code = dto.getCode();
        EmailType type = EmailType.valueOf(dto.getType());

        try {
            Email email = emailRepository.findEmailByCodeAndType(code, type)
                    .orElseThrow(() -> new NoSuchDataException(FIND_EMAIL_WITH_CODE_AND_TYPE_SUCCESS_LOG));
            if (email.isExpired()) {
                throw new IllegalArgumentException("");
            }
            memberService.setMemberEnabled(email.getMember());
            email.setExpired(true);
            emailRepository.save(email);
            logger.info(UPDATE_EMAIL_EXPIRED_SUCCESS_LOG, code, type);
            return true;
        } catch (NoSuchDataException | IllegalArgumentException e) {
            logger.info(UPDATE_EMAIL_EXPIRED_FAILURE_LOG, code, type);
            return false;
        }
    }

    private void expirePreviousMail(String email, EmailType type) {
        try {
            Email data = emailRepository.findEmailByMember_EmailAndTypeAndExpired(email, type, false)
                    .orElseThrow(() -> new NoSuchDataException(VERIFICATION_MAIL_EXPIRED_FAILURE_LOG));
            data.setExpired(true);
            emailRepository.save(data);
            logger.info(VERIFICATION_MAIL_EXPIRED_SUCCESS_LOG, email);
        } catch (NoSuchDataException e) {
            logger.info(e.getMessage(), email);
        }
    }

    private void saveMailDB(String code, EmailType type, Member member) {
        Email email = Email.builder()
                .code(code)
                .type(type)
                .member(member)
                .build();

        emailRepository.save(email);
    }

    private SimpleMailMessage makeVerificationMail(String email, String code, EmailType type) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(VERIFICATION_MAIL_FROM);
        message.setTo(email);
        message.setSubject(VERIFICATION_MAIL_SUBJECT);
        message.setText(makeMailText(code, type.getValue()));

        return message;
    }

    private String makeMailText(String code, String type) {
        return String.format(VERIFICATION_MAIL_TEXT, dynamicHostProvider.getHost(), code, type);
    }

    private String generateRandomCode() {
        return RandomStringUtils.randomAlphanumeric(64);
    }
}
