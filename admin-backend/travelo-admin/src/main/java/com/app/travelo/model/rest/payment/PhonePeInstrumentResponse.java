package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PhonePeInstrumentResponse {
    private String type;
    private PhonePeRedirectInfo redirectInfo;

}