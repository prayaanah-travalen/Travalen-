package com.app.travelo.controller;


import com.app.travelo.model.entity.HotelAmenityEntity;
import com.app.travelo.model.rest.*;
import com.app.travelo.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @PostMapping("/add-amenities")
    public ResponseEntity<HotelAmenityEntity> addAmenity(@RequestBody HotelAmenityEntity amenity) {
        HotelAmenityEntity savedAmenity = hotelService.addAmenity(amenity);
        return ResponseEntity.ok(savedAmenity);
    }

    @GetMapping("/amenities")
    public ResponseEntity<List<HotelAmenityEntity>> getAmenities() {
        return ResponseEntity.ok(hotelService.getAllAmenities());
    }

    @PostMapping(value="/room/save",  consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public  ResponseEntity<ResponseDto<HotelRoomDto> > saveRoom(@RequestPart("room") HotelRoomReqDto room, @RequestPart(name="roomImages", required = false) List<MultipartFile> roomImages) {
        
   	return new ResponseEntity<>( hotelService.saveRoom(room, roomImages), HttpStatus.OK);
    	

    }
    

    @PostMapping(value="/delete")
    public  ResponseEntity<ResponseDto<String>> deleteHotel(@RequestBody HotelRequestDto hotel) {
        return new ResponseEntity<>(hotelService.deleteHotel(hotel), HttpStatus.OK);
    }

    @PostMapping(value="/room/delete")
    public  ResponseEntity<ResponseDto<String> > deleteRoom(@RequestBody HotelRoomReqDto room) {
        return new ResponseEntity<>( hotelService.deleteRoom(room), HttpStatus.OK);
    }

//    @GetMapping("/hotels")
//    public ResponseEntity<List<HotelDto>> getHotels() {
//        return new ResponseEntity<>(hotelService.getHotels(), HttpStatus.OK);
//    }

    @GetMapping("/hotels")
    public ResponseEntity<Page<HotelDto>> getHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<HotelDto> hotels = hotelService.hotels(PageRequest.of(page, size));
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("")
    public ResponseEntity<HotelDto> getHotelById(@RequestParam("id") Long id)  {

        return new ResponseEntity<>(hotelService.getHotelById(id), HttpStatus.OK);
    }
    
    @PostMapping("/contact/save")
    public ResponseEntity<ResponseDto<ContactPersonDto>> saveContactPerson(@RequestBody ContactPersonDto contactPerson) {
        return new ResponseEntity<>(hotelService.saveContactPerson(contactPerson), HttpStatus.OK);
    }

}
