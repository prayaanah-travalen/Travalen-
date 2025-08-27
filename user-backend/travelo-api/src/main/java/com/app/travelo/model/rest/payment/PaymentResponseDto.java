package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentResponseDto {

    private String paymentLink;
    private String paymentId;
    private Long bookingId;
    private String orderId;
}
