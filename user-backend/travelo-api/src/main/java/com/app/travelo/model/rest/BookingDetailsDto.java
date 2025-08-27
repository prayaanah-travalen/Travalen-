package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BookingDetailsDto {
    private HotelDto hotelCode;
//    private HotelRoomDto hotelRoomCode;
    private String checkIn;
    private String checkOut;
    private Integer guestCount;
    private Integer adult;
    private Integer children;
    private Integer noOfRooms;
    private LocationDto location;
}
