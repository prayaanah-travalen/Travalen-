package com.app.travelo.controller;

import com.app.travelo.model.rest.BookingDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.payment.PaymentResponseDto;
import com.app.travelo.model.rest.payment.PaymentDto;
import com.app.travelo.service.booking.BookingService;
import com.app.travelo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/payment")
    public ResponseEntity<ResponseDto<PaymentResponseDto>> hotelBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.hotelBooking(req), HttpStatus.OK);

    }

    @PostMapping("/temporary-booking")
    public ResponseEntity<ResponseDto<BookingDto>> hotelTemporaryBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.temporaryBooking(req), HttpStatus.OK);

    }

    @PostMapping("/cancel")
    public ResponseEntity<ResponseDto<String>> cancelBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.cancelBooking(req), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<BookingDto>> getBookings() {
        return new ResponseEntity<>(bookingService.getBooking(CommonUtil.getLoginUserPhone()), HttpStatus.OK);
    }

    @GetMapping("status")
    public ResponseEntity<PaymentDto> getStatus(@RequestParam("id") Long id) {
        return  new ResponseEntity<>(bookingService.getStatus(id), HttpStatus.OK);
    }

    @PostMapping("/postpone")
    public ResponseEntity<ResponseDto<String>> postponeBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.postponeBooking(req), HttpStatus.OK);
    }

    @PostMapping("/details")
    public ResponseEntity<ResponseDto<BookingDto>> addHotelsForBooking(@RequestBody BookingDto req) {
        return new ResponseEntity<>(bookingService.addHotelsForBooking(req), HttpStatus.OK);

    }

    @GetMapping("/{bookingId}/details")
    public ResponseEntity<ResponseDto<BookingDto>> getBookingDetail(@PathVariable("bookingId") Long bookingId) {
        return new ResponseEntity<>(bookingService.getBookingDetail(bookingId), HttpStatus.OK);

    }

}
