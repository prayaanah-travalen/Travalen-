package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelRoomReqDto {

    private Long hotelCode;

    private RoomDetailsDto roomDetails;
}


