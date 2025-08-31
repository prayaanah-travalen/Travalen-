package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PhonePeData {
    private String merchantId;
    private String merchantTransactionId;
    private  PhonePeInstrumentResponse instrumentResponse;
    private String state;

}