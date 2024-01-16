package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.dto.MemberRequest;

public interface MemberService {
    boolean register(MemberRequest.RegisterDto dto);

    boolean withdraw(MemberRequest.WithdrawDto dto);

    boolean changePassword(MemberRequest.ChangePasswordDto dto);

    boolean checkPasswordMatched(String rawPassword, String encodedPassword);

    boolean checkUsernameDuplicated(String username);

    boolean checkEmailDuplicated(String email);
}
