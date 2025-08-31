package com.app.travelo.services;

import com.app.travelo.model.LocationDto;
import com.app.travelo.model.PopularDestDto;
import com.app.travelo.model.rest.ResponseDto;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface PopularDestService {
    void savePopularDest(PopularDestDto popularDest, MultipartFile file);


    List<PopularDestDto> getPopularDest();

    ResponseDto<String> deleteDestination(PopularDestDto popularDest);

    List<LocationDto> getLocation1();


    void saveLocation(LocationDto location);

    List<LocationDto> getLocation();
}