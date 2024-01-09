package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.dto.MemberRequest;

public interface MemberService {
    boolean register(MemberRequest.RegisterDto dto);
    boolean withdraw(MemberRequest.WithdrawDto dto);
    boolean changePassword(MemberRequest.ChangePasswordDto dto);
    boolean checkUsername(String username);
    boolean checkEmail(String email);
}
