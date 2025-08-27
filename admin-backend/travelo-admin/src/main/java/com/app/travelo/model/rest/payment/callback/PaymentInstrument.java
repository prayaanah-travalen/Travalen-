package com.app.travelo.model.rest.payment.callback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentInstrument {
    public String type;
    public String cardType;
    public String pgTransactionId;
    public String bankTransactionId;
    public String pgAuthorizationCode;
    public String arn;
    public String bankId;
}
