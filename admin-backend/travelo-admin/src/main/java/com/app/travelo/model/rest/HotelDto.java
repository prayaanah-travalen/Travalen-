package com.app.travelo.model.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelDto {
    private Long hotelCode;
    private String hotelName;
    private LocationDto location;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String starRating;
    private List<String> amenities;
    private String about;
    private String websiteLink;
    private String propertyRule;
    private List<HotelRoomDto> hotelRooms;
    private List<HotelImageDto> hotelImages;
    private String email;
    private String latitude;
    private String longitude;
}
