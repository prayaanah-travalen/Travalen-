package com.app.travelo.model.rest.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PayLinkResponseDto {

    private String paymentLink;
    private String paymentId;
    private Long bookingId;
}
