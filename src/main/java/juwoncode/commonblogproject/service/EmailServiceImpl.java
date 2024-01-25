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

    /**
     * 생성한 인증 메일을 메일주소로 전송한다.<br>
     * {@link SimpleMailMessage} 타입의 메일을 {@link JavaMailSender} 객체를 사용하여 전송한다.
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
     * 새로운 인증 메일을 생성하고 반환한다.<br>
     * {@link SimpleMailMessage} 인스턴스를 생성하고 제목, 내용, 수신 주소, 송신 주소를 설정하고 반환한다.
     * @param email
     *      회원 메일 주소.
     * @param code
     *      인증 메일 코드.
     * @param type
     *      인증 메일 타입.
     * @return
     *      설정이 끝난 인증 메일 인스턴스.
     */
    private SimpleMailMessage makeVerificationMail(String email, String code, EmailType type) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(VERIFICATION_MAIL_FROM);
        message.setTo(email);
        message.setSubject(VERIFICATION_MAIL_SUBJECT);
        message.setText(makeMailText(code, type.getValue()));

        return message;
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
        return String.format(VERIFICATION_MAIL_TEXT, dynamicHostProvider.getHost(), code, type);
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
