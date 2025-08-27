package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PhonePeRequestDto {
    private String merchantId;
    private String merchantTransactionId;
    private String merchantUserId;
    private String amount;
    private String redirectUrl;
    private String redirectMode;
    private String callbackUrl;
    private String mobileNumber;
    private PaymentInstrument paymentInstrument;

    public   static PaymentInstrument toPaymentInstrument(String instType) {
        return PaymentInstrument.builder().type(instType).build();
    }

}

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
class PaymentInstrument {
    private String type;
}