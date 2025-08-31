package com.app.travelo.controller;

import com.app.travelo.model.rest.BookingDto;
import com.app.travelo.model.rest.CalendarReqDto;
import com.app.travelo.model.rest.CalendarResDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @GetMapping("")
    public ResponseEntity<List<BookingDto>> getBookings() {
        return new ResponseEntity<>(bookingService.getBooking(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<List<CalendarResDto>> getBookingsForCalendar(@RequestBody CalendarReqDto req) {
        return new ResponseEntity<>(bookingService.getBookingsForCalendar(req), HttpStatus.OK);
    }

    @PostMapping("close")
    public ResponseEntity<ResponseDto<String>> closeBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.closeBooking(req), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<ResponseDto<String>> cancelBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.cancelBooking(req), HttpStatus.OK);
    }

}
