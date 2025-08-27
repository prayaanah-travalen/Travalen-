package com.app.travelo.service.booking;

import com.app.travelo.model.rest.BookingDto;
import com.app.travelo.model.rest.payment.PaymentResponseDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.payment.PaymentDto;

import java.util.List;

public interface BookingService {
    ResponseDto<PaymentResponseDto> hotelBooking(BookingDto req);

    ResponseDto<String> cancelBooking(BookingDto req);

    List<BookingDto> getBooking(String phone);

    PaymentDto getStatus(Long id);

    ResponseDto<String> postponeBooking(BookingDto req);

    ResponseDto<BookingDto> temporaryBooking(BookingDto req);

    ResponseDto<BookingDto> addHotelsForBooking(BookingDto req);

    ResponseDto<BookingDto> getBookingDetail(Long bookingId);
}
