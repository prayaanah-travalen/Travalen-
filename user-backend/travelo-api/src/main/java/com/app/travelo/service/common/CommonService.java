package com.app.travelo.service.common;

import com.app.travelo.model.rest.LocationDto;
import com.app.travelo.model.rest.PopularDestDto;
import com.app.travelo.model.rest.PriceSlabDto;

import java.util.List;

public interface CommonService {
    List<LocationDto> getLocation();
    List<PopularDestDto> getPopularDest();

    List<PriceSlabDto> getPriceSlab();
}
