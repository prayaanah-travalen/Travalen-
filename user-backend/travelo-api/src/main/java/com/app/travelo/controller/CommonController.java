package com.app.travelo.controller;

import com.app.travelo.model.rest.LocationDto;
import com.app.travelo.model.rest.PopularDestDto;
import com.app.travelo.model.rest.PriceSlabDto;
import com.app.travelo.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/util")
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/location")
    public ResponseEntity<List<LocationDto>> getLocationDto() {
        return new ResponseEntity<List<LocationDto>>(commonService.getLocation(), HttpStatus.OK);

    }

    @GetMapping("/popularDest")
    public ResponseEntity<List<PopularDestDto>> getPopularDest() {
        return new ResponseEntity<List<PopularDestDto>>(commonService.getPopularDest(), HttpStatus.OK);

    }

    @GetMapping("/priceSlab")
    public ResponseEntity<List<PriceSlabDto>> getPriceSlab() {
        return new ResponseEntity<>(commonService.getPriceSlab(), HttpStatus.OK);
    }


}
