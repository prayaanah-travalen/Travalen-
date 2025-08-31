package com.app.travelo.services;

import com.app.travelo.model.rest.PriceSlabDto;
import com.app.travelo.model.rest.ResponseDto;

import java.util.List;

public interface PriceSlabService {
    ResponseDto<String> savePriceSlab(PriceSlabDto priceslab);

    List<PriceSlabDto> getPriceSlab();

    ResponseDto<String> deletePriceSlab(PriceSlabDto priceslab);


}


