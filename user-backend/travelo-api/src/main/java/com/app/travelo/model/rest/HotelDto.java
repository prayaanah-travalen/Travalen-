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
    private List<HotelRoomDto> rooms;
    private List<HotelImageDto> hotelImages;
    private String about;
    private List<String> amenities;
    private String websiteLink;
    private String propertyRule;
    private String email;
}
