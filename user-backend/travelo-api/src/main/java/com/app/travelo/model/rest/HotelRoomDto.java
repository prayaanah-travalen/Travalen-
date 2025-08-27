package com.app.travelo.model.rest;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

    private  String status;

    private List<String> amenities;

    private List<String> roomTags;

    private Integer noOfRooms;

    private Integer noOfAvailableRooms;

}
