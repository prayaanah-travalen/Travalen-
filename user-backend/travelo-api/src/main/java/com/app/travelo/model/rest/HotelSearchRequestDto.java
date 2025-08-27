package com.app.travelo.model.rest;

import lombok.Data;

import java.util.List;

@Data
public class HotelSearchRequestDto {
    private Long hotelCode;
    private List<Long> locationId;
    private String checkIn;
    private String checkOut;
    private String adultCount;
    private String childrenCount;
    private List<PriceSlabDto> priceSlab;
    private Integer noOfRooms;
    private Integer guestCount;
}
