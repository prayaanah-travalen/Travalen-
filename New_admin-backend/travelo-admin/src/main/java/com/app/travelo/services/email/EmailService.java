package com.app.travelo.services.email;

import com.app.travelo.model.common.EmailDetailDto;
import org.thymeleaf.context.Context;

public interface EmailService {

    String sendSimpleMail(EmailDetailDto details);

    String sendMailWithAttachment(EmailDetailDto details);

    void sendEmailWithHtmlTemplate(EmailDetailDto details);
}
