package com.app.travelo.controller;


import com.app.travelo.model.rest.*;
import com.app.travelo.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping(value="/save",  consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public  ResponseEntity<ResponseDto<HotelDto>> saveHotel(@RequestPart("hotel") HotelRequestDto hotel, @RequestPart(name="hotelImages", required = false) List<MultipartFile> hotelImages) {
        return new ResponseEntity<>(hotelService.saveHotel(hotel, hotelImages), HttpStatus.OK);
    }

    @PostMapping(value="/room/save",  consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public  ResponseEntity<ResponseDto<HotelRoomDto> > saveRoom(@RequestPart("room") HotelRoomReqDto room, @RequestPart(name="roomImages", required = false) List<MultipartFile> roomImages) {
        
   	return new ResponseEntity<>( hotelService.saveRoom(room, roomImages), HttpStatus.OK);
    	

    }
    
//    @PostMapping(value="/room/save", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ResponseEntity<ResponseDto<HotelRoomSaveResponseDto>> saveRoom(
//            @RequestPart("room") HotelRoomReqDto room,
//            @RequestPart(name="roomImages", required = false) List<MultipartFile> roomImages) {
//
//        return ResponseEntity.ok(hotelService.saveRoom(room, roomImages));
//    }


    @PostMapping(value="/delete")
    public  ResponseEntity<ResponseDto<String>> deleteHotel(@RequestBody HotelRequestDto hotel) {
        return new ResponseEntity<>(hotelService.deleteHotel(hotel), HttpStatus.OK);
    }

    @PostMapping(value="/room/delete")
    public  ResponseEntity<ResponseDto<String> > deleteRoom(@RequestBody HotelRoomReqDto room) {
        return new ResponseEntity<>( hotelService.deleteRoom(room), HttpStatus.OK);
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getHotels() {
        return new ResponseEntity<>(hotelService.getHotels(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<HotelDto> getHotelById(@RequestParam("id") Long id)  {

        return new ResponseEntity<>(hotelService.getHotelById(id), HttpStatus.OK);
    }

}
