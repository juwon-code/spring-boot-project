package juwoncode.commonblogproject.service;

import static juwoncode.commonblogproject.dto.EmailRequest.*;

public interface EmailService {
    void sendVerifyMail(SendDto dto);

    boolean checkVerifyMail(CheckDto dto);
}
