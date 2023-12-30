package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.dto.MemberDto;

public interface MemberService {
    boolean register(MemberDto.RequestDto dto);
    boolean withdraw(MemberDto.WithdrawRequestDto dto);
    boolean changePassword(MemberDto.ChangePasswordRequestDto dto);
}
