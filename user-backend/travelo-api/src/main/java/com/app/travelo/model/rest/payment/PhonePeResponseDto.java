package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PhonePeResponseDto {
    private Boolean success;
    private String code;
    private String message;
    private PhonePeData data;

}



