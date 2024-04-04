package juwoncode.commonblogproject.service;

import jakarta.transaction.Transactional;
import juwoncode.commonblogproject.domain.UserEntity;
import juwoncode.commonblogproject.dto.MailRequestDto;
import juwoncode.commonblogproject.dto.UserRequestDto;
import juwoncode.commonblogproject.dto.UserResponseDto;

public interface UserService {
    @Transactional
    UserResponseDto.Register register(UserRequestDto.Register requestDto);

    UserResponseDto.CheckDuplication checkUsernameDuplicated(String username);

    UserResponseDto.CheckDuplication checkEmailDuplicated(String email);

    @Transactional
    UserResponseDto.Activate activate(UserRequestDto.Activate requestDto);

    @Transactional
    UserResponseDto.Delete delete(UserRequestDto.Delete requestDto);

    @Transactional
    UserResponseDto.ChangePassword changePassword(UserRequestDto.ChangePassword requestDto);
}
