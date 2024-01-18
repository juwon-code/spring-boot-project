package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.domain.Email;
import juwoncode.commonblogproject.domain.Member;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.EmailRepository;
import juwoncode.commonblogproject.vo.EmailType;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static juwoncode.commonblogproject.dto.EmailRequest.*;
import static juwoncode.commonblogproject.vo.LoggerMessage.*;

@Service
public class EmailServiceImpl implements EmailService {
    private final MemberService memberService;
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender javaMailSender, MemberService memberService) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.memberService = memberService;
    }

    @Override
    public void sendVerifyMail(SendDto dto) {
        // 1. 파라미터를 지역변수로 받아온다.
        String email = dto.getEmail();
        EmailType type = EmailType.valueOf(dto.getType());
        String code = generateRandomCode();
        Member member = memberService.getMemberByEmail(email);

        // 2. 이전 메일이 존재할 경우 삭제한다.
        setPreviousMailExpired(email, type);

        // 3. 메일을 DB에 저장한다.
        saveMailDB(code, type, member);

        // 4. 메일을 전송한다.
        javaMailSender.send(createRegisterMail(email, code));
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

    private void setPreviousMailExpired(String email, EmailType type) {
        try {
            Email data = emailRepository.findEmailByMember_EmailAndTypeAndExpired(email, type, false)
                    .orElseThrow(() -> new NoSuchDataException("만료되지 않은 인증 메일이 존재하지 않습니다. 사유: 유효한 메일 없음. 이메일: {}"));
            data.setExpired(true);
            emailRepository.save(data);
            logger.info("이전 인증 메일을 만료했습니다. 이메일: {}", email);
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

    private SimpleMailMessage createRegisterMail(String email, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("juwoncode@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("[common-blog-project] 블로그 프로젝트에 오신것을 환영합니다.");
        simpleMailMessage.setText("<h1>회원가입 인증 메일입니다.</h1>"
                + "<p>회원가입을 완료하기 위해 아래 링크를 클릭하세요.</p>"
                + "<a href='http://localhost:8080/?email=" + email + "&code=" + code + "'>이메일 인증 확인</a>"
                + "<p>감사합니다!</p>");

        return simpleMailMessage;
    }

    private String generateRandomCode() {
        return RandomStringUtils.randomAlphanumeric(64);
    }
}
