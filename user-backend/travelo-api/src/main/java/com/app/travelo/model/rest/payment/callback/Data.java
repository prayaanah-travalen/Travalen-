package com.app.travelo.model.rest.payment.callback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Data {
    public String merchantId;
    public String merchantTransactionId;
    public String transactionId;
    public int amount;
    public String state;
    public String responseCode;
    public PaymentInstrument paymentInstrument;
}
