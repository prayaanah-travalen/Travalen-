package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RefundReqDto {
    public String merchantId;
    public String merchantUserId;
    public String originalTransactionId;
    public String merchantTransactionId;
    public String amount;
    public String callbackUrl;
}
