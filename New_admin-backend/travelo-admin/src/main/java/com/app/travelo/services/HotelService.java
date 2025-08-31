package com.app.travelo.services;

import com.app.travelo.model.rest.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    ResponseDto<HotelDto> saveHotel(HotelRequestDto hotel, List<MultipartFile> hotelImages);

    List<HotelDto> getHotels();

    ResponseDto<HotelRoomDto>  saveRoom(HotelRoomReqDto room, List<MultipartFile> roomImages);

    HotelDto getHotelById(Long id);

    ResponseDto<String> deleteRoom(HotelRoomReqDto room);

    ResponseDto<String> deleteHotel(HotelRequestDto hotel);
}
