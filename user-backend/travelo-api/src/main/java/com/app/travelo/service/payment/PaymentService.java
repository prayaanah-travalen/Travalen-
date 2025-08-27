package com.app.travelo.service.payment;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.entity.PaymentEntity;
import com.app.travelo.model.rest.payment.PaymentResponseDto;
import com.app.travelo.model.rest.payment.PaymentDto;
import com.app.travelo.model.rest.payment.PaymentRequestDto;
import com.app.travelo.model.rest.payment.callback.CallBackRequest;

public interface PaymentService {
    PaymentDto getPaymentId();

    PaymentResponseDto getPaymentLink(PaymentEntity paymentEntity, String amount, String phone);

    PaymentResponseDto createPayment(PaymentRequestDto requestDto);

    PaymentDto getStatus(CallBackRequest req);

    void refund(BookingEntity booking);

    PaymentDto getStatusOfBooking(Long id);

}
