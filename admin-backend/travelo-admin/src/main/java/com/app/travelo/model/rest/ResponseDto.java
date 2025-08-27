package com.app.travelo.model.rest;

import lombok.*;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class ResponseDto<T> {
    private String errorCode;
    private String errorMessage;
    private String success;
    private T response;


}
