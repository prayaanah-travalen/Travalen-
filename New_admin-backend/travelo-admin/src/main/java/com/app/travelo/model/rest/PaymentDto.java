package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentDto {

    private String paymentId;
    private String status;
    private String transactionType;
    private String paymentDate;
}
