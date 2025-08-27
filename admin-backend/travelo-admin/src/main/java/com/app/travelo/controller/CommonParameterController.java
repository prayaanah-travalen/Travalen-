package com.app.travelo.controller;

import com.app.travelo.model.rest.CommonParameterDto;
import com.app.travelo.model.rest.RoomDto;
import com.app.travelo.services.CommonParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CommonParameterController {

    @Autowired
    private CommonParameterService commonService;

    @PostMapping("/room/save")
    public void saveRoom(@RequestBody RoomDto req) {
        commonService.saveRoom(req);
    }

    @GetMapping("room-package")
    public ResponseEntity<List<CommonParameterDto>> getRoomPackage(){
        return new ResponseEntity<>(commonService.getRoomPackage(), HttpStatus.OK);
    }

    @GetMapping("amenities")
    public ResponseEntity<List<CommonParameterDto>> getAmenities(){
        return new ResponseEntity<>(commonService.getAmenities(), HttpStatus.OK);
    }

    @GetMapping("room-amenities")
    public ResponseEntity<List<CommonParameterDto>> getRoomAmenities(){
        return new ResponseEntity<>(commonService.getRoomAmenities(), HttpStatus.OK);
    }

}
