package juwoncode.commonblogproject.service;

import static juwoncode.commonblogproject.dto.EmailRequest.*;

public interface EmailService {
    /**
     * 인증 메일을 생성하고 전송한다.<br>
     * 이전 인증 메일을 만료하고, DB에 저장한 후, 메일을 전송한다.
     * @param dto
     *      {@link SendDto} 객체.
     */
    void sendVerifyMail(SendDto dto);

    /**
     * 인증 메일을 확인하고 결과를 반환한다.<br>
     * 코드, 타입과 일치하는 인증 메일을 조회하고, 만료하고 결과를 반환한다.
     * @param dto
     *      {@link CheckDto} 객체.
     * @return
     *      메일 인증 결과.
     */
    boolean checkVerifyMail(CheckDto dto);
}
