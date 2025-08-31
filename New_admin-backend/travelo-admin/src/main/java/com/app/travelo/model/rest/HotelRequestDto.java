package com.app.travelo.model.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelRequestDto {
    private Long hotelCode;
    private String hotelName;
    private String location;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String starRating;
    private List<HotelRoomReqDto> hotelRooms;
    private List<String> amenities;
    private String about;
    private String websiteLink;
    private String propertyRule;
    private List<HotelImageReqDto> images;
    private String email;
    private List<Long> deletedImages;
    private String latitude;
    private String longitude;
}
