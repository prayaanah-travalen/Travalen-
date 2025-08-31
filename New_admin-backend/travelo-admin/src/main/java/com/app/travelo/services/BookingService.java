package com.app.travelo.services;

import com.app.travelo.model.rest.BookingDto;
import com.app.travelo.model.rest.CalendarReqDto;
import com.app.travelo.model.rest.CalendarResDto;
import com.app.travelo.model.rest.ResponseDto;

import java.util.List;
import java.util.Map;

public interface BookingService {
    List<BookingDto> getBooking();

    List<CalendarResDto> getBookingsForCalendar(CalendarReqDto req);

    ResponseDto<String> closeBooking(BookingDto req);
    ResponseDto<String> cancelBooking(BookingDto req);
}
