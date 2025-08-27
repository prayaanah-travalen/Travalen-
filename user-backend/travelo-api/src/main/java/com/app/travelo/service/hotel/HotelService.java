package com.app.travelo.service.hotel;

import com.app.travelo.model.rest.HotelDto;
import com.app.travelo.model.rest.HotelSearchRequestDto;

import java.util.List;

public interface HotelService {
    List<HotelDto> getHotels(HotelSearchRequestDto req);

    HotelDto getHotelById(Long id);
}
