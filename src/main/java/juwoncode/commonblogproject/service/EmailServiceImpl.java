package juwoncode.commonblogproject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import juwoncode.commonblogproject.util.DynamicHostProvider;
import juwoncode.commonblogproject.util.LoggerProvider;
import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.EmailRepository;
import juwoncode.commonblogproject.vo.EmailType;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static juwoncode.commonblogproject.dto.EmailRequest.*;
import static juwoncode.commonblogproject.vo.LoggerMessage.*;
import static juwoncode.commonblogproject.vo.MailConstants.*;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final DynamicHostProvider dynamicHostProvider;
    private final Logger logger = LoggerProvider.getLogger(this.getClass());

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender javaMailSender, DynamicHostProvider dynamicHostProvider) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.dynamicHostProvider = dynamicHostProvider;
    }


    @Override
    public void sendVerifyMail(SendDto dto) {
        String email = dto.getEmail();
        EmailType type = EmailType.valueOf(dto.getType());
        String code = generateRandomCode();
        Member member = dto.getMember();

        expirePreviousMail(email, type);

        saveMailDB(code, type, member);

        sendMail(email, code, type);
    }

    /**
     * 인증 메일을 생성하고 메일 주소로 전송한다.<br>
     * {@link MimeMessage} 타입의 메일을 {@link JavaMailSender} 객체를 사용하여 전송한다.
     * @param email
     *      수신 메일 주소
     * @param code
     *      인증 메일 코드
     * @param type
     *      인증 메일 타입
     * @see 
     *      JavaMailSender#send(SimpleMailMessage)
     */
    private void sendMail(String email, String code, EmailType type) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

            messageHelper.setFrom(VERIFICATION_MAIL_FROM);
            messageHelper.setTo(email);
            messageHelper.setSubject(VERIFICATION_MAIL_SUBJECT);
            messageHelper.setText(makeMailText(code, type.getValue()), true);

            logger.info(SEND_EMAIL_SUCCESS_LOG, email);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.info(SEND_EMAIL_FAILURE_LOG, email);
            e.printStackTrace();
        }
    }

    @Override
    public Member expireVerifyMail(ExpirationDto dto) {
        String code = dto.getCode();
        EmailType type = EmailType.valueOf(dto.getType());

        try {
            Email email = emailRepository.findEmailByCodeAndType(code, type)
                    .orElseThrow(() -> new NoSuchDataException(CANNOT_FOUND_VERIFY_MAIL_LOG));

            if (email.isExpired()) {
                throw new IllegalArgumentException(UPDATE_EMAIL_EXPIRED_FAILURE_LOG);
            }

            email.setExpired(true);
            emailRepository.save(email);
            logger.info(UPDATE_EMAIL_EXPIRED_SUCCESS_LOG, code, type);
            return email.getMember();
        } catch (NoSuchDataException | IllegalArgumentException e) {
            logger.info(e.getMessage(), code, type);
            throw e;
        }
    }

    /**
     * 회원 메일 주소와 일치하는 이전 인증 메일을 만료한다.<br>
     * 회원 메일 주소, 인증 메일 타입과 일치하는 만료되지 않은 메일을 조회하고,
     * 결과가 존재할 경우 만료하고 저장한다.
     * @param email
     *      회원 메일 주소
     * @param type
     *      인증 메일 타입
     * @see
     *      EmailRepository#findEmailByMember_EmailAndTypeAndExpired(String, EmailType, boolean)
     * @see
     *      EmailRepository#save(Object)
     */
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

    /**
     * 인증 메일을 데이터베이스에 저장한다.
     * @param code
     *      인증 메일 코드.
     * @param type
     *      인증 메일 타입.
     * @param member
     *      회원 엔티티 객체.
     * @see
     *      EmailRepository#save(Object)
     */
    private void saveMailDB(String code, EmailType type, Member member) {
        Email email = Email.builder()
                .code(code)
                .type(type)
                .member(member)
                .build();

        emailRepository.save(email);
    }

    /**
     * 인증 메일 내용을 생성하고 반환한다.<br>
     * 인증 메일 내용에 인증 확인 링크를 첨부하여 반환한다.
     * @param code
     *      인증 메일 코드.
     * @param type
     *      인증 메일 타입.
     * @return
     *      인증 메일 내용.
     * @see
     *      DynamicHostProvider#getHost()
     */
    private String makeMailText(String code, String type) {
        String host = dynamicHostProvider.getHost();
        String encodedCode = encodeUrlParam(code);
        String encodedType = encodeUrlParam(type);

        return String.format(VERIFICATION_MAIL_TEXT, host, encodedCode, encodedType);
    }

    private String encodeUrlParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }

    /**
     * 64 자리의 인증 메일 코드를 생성하고 반환한다.
     * @return
     *      인증 메일 코드.
     */
    private String generateRandomCode() {
        return RandomStringUtils.randomAlphanumeric(64);
    }
}
