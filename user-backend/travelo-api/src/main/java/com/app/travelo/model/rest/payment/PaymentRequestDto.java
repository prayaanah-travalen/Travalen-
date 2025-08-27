package com.app.travelo.model.rest.payment;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.entity.GuestEntity;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PaymentRequestDto {

    private String paymentId;

    private String parentPaymentId;

    private BookingEntity bookingId;

    private String transactionNo;

    private String transactionType;

    private String status;

    private String paymentMethod;

    private String paymentType;

    private GuestEntity guest;

    private String bankId;

    private String authTransactionId;

    private String amount;

    private String merchantTransactionId;

    private String orderId;
}
