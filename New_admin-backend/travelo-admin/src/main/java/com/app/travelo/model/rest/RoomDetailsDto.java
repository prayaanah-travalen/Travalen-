package com.app.travelo.model.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RoomDetailsDto {
    private Long hotelRoomId;

    private String roomName;

    private Integer occupancy;

    private String roomDescription;

    private String price;

    private String bedType;

    private List<Long> priceSlab;

    private List<String> amenities;

    private List<String> roomTags;

    private List<Long> deletedImages;

    private Integer noOfRooms;

    private String roomPackage;

    private String extraBedCostAdult;
    private String extraBedCostChild;

}
