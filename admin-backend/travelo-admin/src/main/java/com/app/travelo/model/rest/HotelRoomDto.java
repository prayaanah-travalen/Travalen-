package com.app.travelo.model.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelRoomDto {

    private Long hotelRoomId;

    private Long hotelCode;

    private String roomName;

    private Integer occupancy;

    private String roomDescription;

    private String price;

    private String bedType;

    private List<HotelImageDto> roomImages;

    private List<PriceSlabRoomDto> priceSlab;
    private List<String> amenities;

    private List<String> roomTags;

    private Integer noOfRooms;

    private Integer noOfAvailableRooms;

    private String roomPackage;

    private String extraBedCostAdult;

    private String extraBedCostChild;
}
