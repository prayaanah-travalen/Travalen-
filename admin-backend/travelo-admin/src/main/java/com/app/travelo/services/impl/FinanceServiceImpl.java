package com.app.travelo.services.impl;

import com.app.travelo.model.entity.FinanceEntity;
import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.rest.FinanceDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.repositories.FinanceRepository;
import com.app.travelo.repositories.HotelRepository;
import com.app.travelo.services.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private HotelRepository hotelRepo;

    @Autowired
    private FinanceRepository financeRepo;

    @Override
    public ResponseDto<FinanceDto> saveFinance(FinanceDto req) {
        Optional<HotelEntity> hotel = hotelRepo.findById(req.getHotelCode());
        ResponseDto<FinanceDto> response = new ResponseDto<>();
        if(hotel.isPresent()) {
            FinanceEntity entity = new FinanceEntity();
            if(Objects.nonNull(req.getId())) {
                Optional<FinanceEntity> existing = financeRepo.findById(req.getId());
                if(existing.isPresent()) {
                    entity = existing.get();
                }
            }
            entity.setAccountHolderName(req.getAccountHolderName());
            entity.setAccountNumber(req.getAccountNumber());
            entity.setIfsc(req.getIfsc());
            entity.setBankName(req.getBankName());
            entity.setGstIn(req.getGstIn());
            entity.setHotel(hotel.get());
            entity.setCountry(req.getCountry());
            entity.setRegisteredForGst(req.getRegisteredForGst());
            entity.setTradeName(req.getTradeName());
            entity.setSwiftCode(req.getSwiftCode());
            entity.setPan(req.getPan());

            entity = financeRepo.save(entity);

            response.setSuccess("SUCCESS");
            response.setResponse(toFinanceDto(entity));

        } else {
            response.setErrorCode("400");
            response.setErrorMessage("Hotel not found");
        }


        return response;
    }

    @Override
    public ResponseDto<FinanceDto> getFinance(FinanceDto req) {
        ResponseDto<FinanceDto> response = new ResponseDto<>();
        FinanceEntity existing = financeRepo.getHotelFinance(req.getHotelCode());
        if(Objects.nonNull(existing)) {
            response.setResponse(toFinanceDto(existing));
            response.setSuccess("SUCCESS");
        } else {
            response.setErrorCode("400");
            response.setErrorMessage("Resource not found");
        }
        return response;
    }

    private FinanceDto toFinanceDto(FinanceEntity entity) {
        return FinanceDto.builder()
                .id(entity.getId())
                .accountHolderName(entity.getAccountHolderName())
                .accountNumber(entity.getAccountNumber())
                .ifsc(entity.getIfsc())
                .bankName(entity.getBankName())
                .gstIn(entity.getGstIn())
                .hotelCode(entity.getHotel().getHotelCode())
                .country(entity.getCountry())
                .registeredForGst(entity.getRegisteredForGst())
                .tradeName(entity.getTradeName())
                .swiftCode(entity.getSwiftCode())
                .pan(entity.getPan())
            .build();
    }
}
