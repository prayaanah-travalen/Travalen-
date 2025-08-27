package com.app.travelo.services.impl;

import com.app.travelo.model.entity.PriceSlabEntity;
import com.app.travelo.model.rest.PriceSlabDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.repositories.PriceSlabRepository;
import com.app.travelo.services.PriceSlabService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceSlabServiceImpl implements PriceSlabService {
    @Autowired
    private PriceSlabRepository priceRepo;

    @Override
    public ResponseDto<String> savePriceSlab(PriceSlabDto priceSlab) {
        ResponseDto<String> response = new ResponseDto<>();


        if(Objects.isNull(priceSlab.getId())) {
            PriceSlabEntity existing = priceRepo.findByPriceSlab(priceSlab.getPriceSlab());
            if(Objects.nonNull(existing)) {
                response.setErrorCode("PS01");
                response.setErrorMessage("Price slab already exists");
                return  response;
            }
        }

        PriceSlabEntity priceSlabEntity = PriceSlabEntity.builder()
                .id(priceSlab.getId())
                .priceSlab(priceSlab.getPriceSlab())
                .maxAllowedGuest(priceSlab.getMaxAllowedGuest())
                .maxAllowedRoom(priceSlab.getMaxAllowedRoom())
                .noOfNights(priceSlab.getNoOfNights())
                .build();

        if(Objects.nonNull(priceSlab.getId())) {
            Optional<PriceSlabEntity> existingEntity = priceRepo.findById(priceSlab.getId());
            existingEntity.ifPresent(slabEntity -> priceSlabEntity.setId(slabEntity.getId()));
        }

       priceRepo.save(priceSlabEntity);

        response.setSuccess("SUCCESS");
        return  response;
    }

    @Override
    public List<PriceSlabDto> getPriceSlab()  {
        List<PriceSlabEntity> priceSlabList = priceRepo.findAll();
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

    @Override
    public ResponseDto<String> deletePriceSlab(PriceSlabDto priceSlab) {
        ResponseDto<String> response = new ResponseDto<>();
        try {

            Optional<PriceSlabEntity> existing = priceRepo.findById(priceSlab.getId());
            if (existing.isPresent()) {
                priceRepo.delete(existing.get());
                response.setSuccess("SUCCESS");
            } else {
                response.setErrorCode("PS01");
                response.setErrorMessage("Price slab already exists");
            }

        }catch (ConstraintViolationException e) {
            response.setErrorCode("PS01");
            response.setErrorMessage("Selected price slab cannot be deleted since it is mapped to hotels");
        }
        return response;

    }


    public List<PriceSlabDto> getPrice_Slab() {
        List<PriceSlabEntity> priceSlabList = priceRepo.findAll();
        return priceSlabList.stream().map(pri->
                PriceSlabDto.builder()
                        .id(pri.getId())
                        .priceSlab(pri.getPriceSlab())
                        .maxAllowedRoom(pri.getMaxAllowedRoom())
                        .maxAllowedGuest(pri.getMaxAllowedGuest())
                        .build()
        ).collect(Collectors.toList());
    }
}







