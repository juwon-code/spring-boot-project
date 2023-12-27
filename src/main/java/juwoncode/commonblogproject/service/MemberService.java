package juwoncode.commonblogproject.service;

import juwoncode.commonblogproject.dto.MemberDto;

public interface MemberService {
    boolean register(MemberDto.RequestDto dto);
}
