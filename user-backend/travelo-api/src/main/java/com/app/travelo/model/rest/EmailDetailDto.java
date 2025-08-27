package com.app.travelo.model.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetailDto {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
