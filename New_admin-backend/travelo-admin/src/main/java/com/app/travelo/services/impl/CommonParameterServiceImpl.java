package com.app.travelo.services.impl;

import com.app.travelo.model.entity.RoomEntity;
import com.app.travelo.model.rest.CommonParameterDto;
import com.app.travelo.model.rest.RoomDto;
import com.app.travelo.repositories.CommonParameterRepository;
import com.app.travelo.repositories.RoomRepository;
import com.app.travelo.services.CommonParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonParameterServiceImpl implements CommonParameterService {
    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private CommonParameterRepository commonParamRepo;

    @Override
    public void saveRoom(RoomDto req) {
        RoomEntity roomEntity = RoomEntity.builder()
                .roomType(req.getRoomName())
                .bedType(req.getBedType().name())
                .build();
        roomRepo.save(roomEntity);
    }

    @Override
    public List<CommonParameterDto> getRoomPackage() {
        return commonParamRepo.getParameters("PACKAGE").stream().map(param->
                    CommonParameterDto.builder()
                            .parameterId(param.getParameterId())
                            .parameter(param.getParameter())
                            .description(param.getDescription())
                            .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<CommonParameterDto> getAmenities() {
        return commonParamRepo.getParameters("AMENITIES").stream().map(param->
                CommonParameterDto.builder()
                        .parameterId(param.getParameterId())
                        .parameter(param.getParameter())
                        .description(param.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<CommonParameterDto> getRoomAmenities() {
        return commonParamRepo.getParameters("ROOM_AMENITIES").stream().map(param->
                CommonParameterDto.builder()
                        .parameterId(param.getParameterId())
                        .parameter(param.getParameter())
                        .description(param.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }
}
