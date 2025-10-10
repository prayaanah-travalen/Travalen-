package com.app.travelo.services;

import com.app.travelo.model.rest.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    ResponseDto<HotelDto> saveHotel(HotelRequestDto hotel, List<MultipartFile> hotelImages);

    List<HotelDto> getHotels();

    Page<HotelDto> hotels(Pageable pageable);

    ResponseDto<HotelRoomDto>  saveRoom(HotelRoomReqDto room, List<MultipartFile> roomImages);
    

    HotelDto getHotelById(Long id);

    ResponseDto<String> deleteRoom(HotelRoomReqDto room);

    ResponseDto<String> deleteHotel(HotelRequestDto hotel);
    
 
    ResponseDto<ContactPersonDto> saveContactPerson(ContactPersonDto contactPerson);
}
