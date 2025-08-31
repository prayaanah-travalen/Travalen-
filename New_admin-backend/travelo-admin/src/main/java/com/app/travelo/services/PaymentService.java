package com.app.travelo.services;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.rest.PaymentDto;

public interface PaymentService {

    PaymentDto getStatus(Long id);

    void refund(BookingEntity booking);

}
