package com.app.travelo.controller;

import com.app.travelo.model.LocationDto;
import com.app.travelo.model.PopularDestDto;
import com.app.travelo.services.PopularDestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api")

public class PopularDestController {

    @Autowired
    private PopularDestService popularDestService;

    @PostMapping(value = "/popularDest/save",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public void savePopularDest(@RequestPart("popularDest") PopularDestDto popularDest, @RequestPart("image") MultipartFile image) {
        popularDestService.savePopularDest(popularDest, image);
    }

    @PostMapping( "/popularDest/delete")
    public void deletePopularDest(@RequestBody() PopularDestDto popularDest) {
        popularDestService.deleteDestination(popularDest);
    }


    @GetMapping("/popularDest")
    public ResponseEntity<List<PopularDestDto>> getPopularDest() {
        return new ResponseEntity<List<PopularDestDto>>(popularDestService.getPopularDest(), HttpStatus.OK);

    }

    @PostMapping("/location/save")
    public void saveLocation(@RequestBody LocationDto location) {
        popularDestService.saveLocation(location);
    }

    @GetMapping("/location")
    public ResponseEntity<List<LocationDto>> getLocationDto() {
        return new ResponseEntity<List<LocationDto>>(popularDestService.getLocation(), HttpStatus.OK);

    }

}
