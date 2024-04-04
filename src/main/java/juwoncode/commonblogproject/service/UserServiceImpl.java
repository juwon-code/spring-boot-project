package juwoncode.commonblogproject.service;

import jakarta.transaction.Transactional;
import juwoncode.commonblogproject.dto.UserRequestDto;
import juwoncode.commonblogproject.dto.UserResponseDto;
import juwoncode.commonblogproject.util.LoggerProvider;
import juwoncode.commonblogproject.domain.UserEntity;
import juwoncode.commonblogproject.dto.MailRequestDto;
import juwoncode.commonblogproject.exception.NoSuchDataException;
import juwoncode.commonblogproject.repository.UserRepository;
import juwoncode.commonblogproject.vo.MailType;
import juwoncode.commonblogproject.vo.RoleType;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static juwoncode.commonblogproject.vo.LoggerMessage.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Logger logger = LoggerProvider.getLogger(this.getClass());

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public UserResponseDto.Register register(UserRequestDto.Register requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        try {
            if (checkUserDuplicated(username, email)) {
                throw new IllegalArgumentException();
            }

            UserEntity savedUser = saveUser(requestDto);

            MailRequestDto.Send sendRequestDto = MailRequestDto.Send.builder()
                    .email(email)
                    .type(MailType.ACTIVATION)
                    .userEntity(savedUser)
                    .build();
            mailService.send(sendRequestDto);

            logger.info("회원가입을 처리하는데 실패했습니다. username=\"{}\"", username);
            return UserResponseDto.Register.builder()
                    .isSuccess(true)
                    .message("회원가입이 완료되었습니다.")
                    .build();
        } catch (IllegalArgumentException e) {
            logger.info("중복된 아이디, 이메일으로 가입할 수 없습니다. username=\"{}\", email=\"{}\"", username, email);
            return UserResponseDto.Register.builder()
                    .isSuccess(false)
                    .message("회원가입에 실패했습니다.")
                    .build();
        }
    }

    private UserEntity saveUser(UserRequestDto.Register dto) {
        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(encryptPassword(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(RoleType.USER)
                .profileImageUrl("DEFAULT_IMAGE_URL")
                .build();

        UserEntity saved = userRepository.save(user);
        logger.info("회원을 성공적으로 저장했습니다.");
        return saved;
    }

    private boolean checkUserDuplicated(String username, String email) {
        return userRepository.existsMemberByUsernameAndEmail(username, email);
    }

    @Override
    @Transactional
    public UserResponseDto.ChangePassword changePassword(UserRequestDto.ChangePassword requestDto) {
        String username = requestDto.getUsername();
        String oldPassword = encryptPassword(requestDto.getOldPassword());
        String newPassword = encryptPassword(requestDto.getNewPassword());

        try {
            UserEntity changeUser = userRepository.findUserEntityByUsernameAndPassword(username, oldPassword)
                    .orElseThrow(() -> new NoSuchElementException("비밀번호 변경에 실패했습니다. 사유: 회원이 존재하지 않거나 비밀번호가 일치하지 않음. username=\"{}\""));
            changeUser.setPassword(newPassword);
            userRepository.save(changeUser);

            logger.info("비밀번호 변경이 성공했습니다. username=\"{}\"", username);
            return UserResponseDto.ChangePassword.builder()
                    .isSuccess(true)
                    .message("비밀번호 변경이 성공했습니다.")
                    .build();
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage(), username);
            return UserResponseDto.ChangePassword.builder()
                    .isSuccess(false)
                    .message("비밀번호 변경에 실패했습니다.")
                    .build();
        }
    }

    private String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public UserResponseDto.CheckDuplication checkUsernameDuplicated(String username) {
        boolean isEmailExist = userRepository.existsByUsername(username);

        if (isEmailExist) {
            return UserResponseDto.CheckDuplication.builder()
                    .isOk(false)
                    .message("중복된 아이디는 사용할 수 없습니다.")
                    .build();
        }

        return UserResponseDto.CheckDuplication.builder()
                .isOk(true)
                .message("사용가능한 아이디 입니다.")
                .build();
    }

    @Override
    public UserResponseDto.CheckDuplication checkEmailDuplicated(String email) {
        boolean isEmailExist = userRepository.existsByEmail(email);

        if (isEmailExist) {
            return UserResponseDto.CheckDuplication.builder()
                    .isOk(false)
                    .message("중복된 이메일은 사용할 수 없습니다.")
                    .build();
        }

        return UserResponseDto.CheckDuplication.builder()
                .isOk(true)
                .message("사용가능한 이메일 입니다.")
                .build();
    }

    @Override
    @Transactional
    public UserResponseDto.Delete delete(UserRequestDto.Delete requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        long deleteCount = userRepository.deleteByUsernameAndPassword(username, password);

        if (deleteCount == 0) {
            logger.info("회원삭제에 실패했습니다. username=\"{}\"", username);
            return UserResponseDto.Delete.builder()
                    .isSuccess(false)
                    .message("회원탈퇴가 실패했습니다.")
                    .build();
        }

        logger.info("회원삭제에 성공했습니다. username=\"{}\"", username);
        return UserResponseDto.Delete.builder()
                .isSuccess(true)
                .message("회원탈퇴가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public UserResponseDto.Activate activate(UserRequestDto.Activate requestDto) {
        String username = requestDto.getUsername();
        String mailCode = requestDto.getCode();
        MailType mailType = requestDto.getType();
        boolean isUserActivated = checkUserActivated(username);

        if (isUserActivated) {
            return UserResponseDto.Activate.builder()
                    .isSuccess(false)
                    .message("이미 활성화된 회원입니다.")
                    .build();
        }

        try {
            UserEntity user = mailService.expire(requestDto);
            user.setActivated(true);

            logger.info("회원이 성공적으로 활성화되었습니다. username=\"{}\"", username);
            return UserResponseDto.Activate.builder()
                    .isSuccess(true)
                    .message("회원이 성공적으로 활성화되었습니다.")
                    .build();
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage(), mailCode, mailType);
            return UserResponseDto.Activate.builder()
                    .isSuccess(false)
                    .message("유효하지 않은 인증링크 입니다.")
                    .build();
        }
    }

    private boolean checkUserActivated(String username) {
        return userRepository.existsByUsernameAndActivated(username, true);
    }
}
