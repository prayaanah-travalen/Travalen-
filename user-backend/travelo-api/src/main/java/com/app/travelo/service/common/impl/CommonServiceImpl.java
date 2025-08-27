package com.app.travelo.service.common.impl;

import com.app.travelo.model.entity.LocationEntity;
import com.app.travelo.model.entity.PopularDestEntity;
import com.app.travelo.model.entity.PriceSlabEntity;
import com.app.travelo.model.rest.LocationDto;
import com.app.travelo.model.rest.PopularDestDto;
import com.app.travelo.model.rest.PriceSlabDto;
import com.app.travelo.repository.LocationRepository;
import com.app.travelo.repository.PopularDestRepository;
import com.app.travelo.repository.PriceSlabRepository;
import com.app.travelo.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private PopularDestRepository popularDestRepo;

    @Autowired
    private PriceSlabRepository priceSlabRepo;

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

    @Override
    public List<PopularDestDto> getPopularDest() {
        List<PopularDestEntity> popularDestList = popularDestRepo.findAll();
        return popularDestList.stream().map(populdst ->
                PopularDestDto.builder()
                        .popularDestId(populdst.getPopularDestId())
                        .location(populdst.getLocation())
                        .url(populdst.getUrl())
                        .image(populdst.getImage())

                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<PriceSlabDto> getPriceSlab() {
        List<PriceSlabEntity> priceSlabList = priceSlabRepo.findAll();
        return priceSlabList.stream().map(pri->
                PriceSlabDto.builder()
                        .id(pri.getId())
                        .priceSlab(pri.getPriceSlab())
                        .maxAllowedRoom(pri.getMaxAllowedRoom())
                        .maxAllowedGuest(pri.getMaxAllowedGuest())
                        .noOfNights(pri.getNoOfNights())
                        .build()
        ).collect(Collectors.toList());
    }
}
