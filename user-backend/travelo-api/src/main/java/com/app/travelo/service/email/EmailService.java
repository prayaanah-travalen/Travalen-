package com.app.travelo.service.email;

import com.app.travelo.model.rest.EmailDetailDto;

public interface EmailService {

    String sendSimpleMail(EmailDetailDto details);

    String sendMailWithAttachment(EmailDetailDto details);

    void sendEmailWithHtmlTemplate(EmailDetailDto details);
}
