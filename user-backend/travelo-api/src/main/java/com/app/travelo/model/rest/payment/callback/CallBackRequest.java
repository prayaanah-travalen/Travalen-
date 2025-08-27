package com.app.travelo.model.rest.payment.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CallBackRequest {
    private String response;

    @JsonProperty("razorpay_payment_id")
    private String merchantPaymentId;

    @JsonProperty("razorpay_order_id")
    private String orderId;

    @JsonProperty("razorpay_signature")
    private String signature;

}
