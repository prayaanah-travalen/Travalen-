package com.app.travelo.controller;

import com.app.travelo.model.rest.PriceSlabDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.services.PriceSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class PriceSlabController {

    @Autowired
    private PriceSlabService priceSlabService;

    @PostMapping("/priceSlab/save")
    public   ResponseEntity<ResponseDto<String>> savePriceSlab(@RequestBody PriceSlabDto priceslab) {
        return new ResponseEntity<>(priceSlabService.savePriceSlab(priceslab), HttpStatus.OK);
    }

    @GetMapping("/priceSlab")
    public ResponseEntity<List<PriceSlabDto>> getPriceSlab() {
        return new ResponseEntity<>(priceSlabService.getPriceSlab(), HttpStatus.OK);
    }

    @PostMapping("/priceSlab/delete")
    public ResponseEntity<ResponseDto<String>> deletePriceSlab(@RequestBody PriceSlabDto priceSlab) {
        return new ResponseEntity<>(priceSlabService.deletePriceSlab(priceSlab), HttpStatus.OK);
    }


}

