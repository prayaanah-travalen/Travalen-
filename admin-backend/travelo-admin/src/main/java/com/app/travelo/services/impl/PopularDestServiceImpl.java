package com.app.travelo.services.impl;


import com.app.travelo.model.LocationDto;
import com.app.travelo.model.PopularDestDto;

import com.app.travelo.model.entity.LocationEntity;
import com.app.travelo.model.entity.PopularDestEntity;

import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.repositories.LocationRepository;
import com.app.travelo.repositories.PopularDestRepository;
import com.app.travelo.services.PopularDestService;
import com.app.travelo.util.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PopularDestServiceImpl implements  PopularDestService {

    @Autowired
    private PopularDestRepository popularDestRepo;

    @Autowired
    private AmazonClient amazonClient;

    public void savePopularDest(PopularDestDto popularDest, MultipartFile file) {
        String url = amazonClient.uploadFile(file);

        PopularDestEntity popularDestEntity = PopularDestEntity.builder()
                .location(popularDest.getLocation())
                .image(url)
                .url(popularDest.getUrl())
                .destName(popularDest.getName())
                .description(popularDest.getDescription())
                .build();
        popularDestRepo.save(popularDestEntity);

    }



    public List<PopularDestDto> getPopularDest() {
        List<PopularDestEntity> popularDestList = popularDestRepo.findAll();
        return popularDestList.stream().map(populdst ->
                PopularDestDto.builder()
                        .popularDestId(populdst.getPopularDestId())
                        .location(populdst.getLocation())
                        .url(populdst.getUrl())
                        .image(populdst.getImage())
                        .name(populdst.getDestName())
                        .description(populdst.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ResponseDto<String> deleteDestination(PopularDestDto popularDest) {
        ResponseDto<String> response = new ResponseDto<>();
        try {

            Optional<PopularDestEntity> popularDestEntity = popularDestRepo.findById(popularDest.getPopularDestId());
            if(popularDestEntity.isPresent()){
                amazonClient.deleteFileFromS3Bucket(popularDest.getImage());
                popularDestRepo.delete(popularDestEntity.get());
                response.setSuccess("SUCCESS");
            } else {
                response.setErrorMessage("Popular destination does not exist");
            }
        } catch(Exception e) {
            response.setErrorMessage("Something went wrong");

        }
       return response;
    }

    @Autowired
    private LocationRepository locationRepo;

    @Override
    public void saveLocation(LocationDto location) {
        LocationEntity locationEntity = LocationEntity.builder()
                .location(location.getLocation())
                .build();
        locationRepo.save(locationEntity);
    }
    @Override
    public List<LocationDto> getLocation1() {
        return null;
    }

    @Override
    public List<LocationDto> getLocation() {
        List<LocationEntity> locationList = locationRepo.findAll();
        return locationList.stream().map(loc->
                LocationDto.builder()
                        .locationId(loc.getLocationId())
                        .location(loc.getLocation())
                        .build()
        ).collect(Collectors.toList());
    }
}



