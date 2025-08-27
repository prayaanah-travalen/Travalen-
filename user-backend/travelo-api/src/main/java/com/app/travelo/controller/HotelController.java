package com.app.travelo.controller;

import com.app.travelo.model.rest.HotelDto;
import com.app.travelo.model.rest.HotelSearchRequestDto;
import com.app.travelo.service.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getHotels(@RequestBody HotelSearchRequestDto req) {
        return new ResponseEntity<>(hotelService.getHotels(req), HttpStatus.OK);
    }

    @GetMapping("/hotel")
    public ResponseEntity<HotelDto> getHotelById(@RequestParam("id") Long id)  {

        return new ResponseEntity<>(hotelService.getHotelById(id), HttpStatus.OK);
    }

}
