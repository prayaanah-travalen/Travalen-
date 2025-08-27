package com.app.travelo.services;

import com.app.travelo.model.rest.CommonParameterDto;
import com.app.travelo.model.rest.RoomDto;

import java.util.List;

public interface CommonParameterService {
    void saveRoom(RoomDto req);

    List<CommonParameterDto> getRoomPackage();

    List<CommonParameterDto> getAmenities();

    List<CommonParameterDto> getRoomAmenities();
}
