package com.app.travelo.model.rest;

import com.app.travelo.model.enums.BookedFor;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BookingDto {
    private Long bookingId;
    private List<BookingDetailsDto> bookingDetails;
    private GuestDetails guestDetails;
    private BookedFor bookedFor;
    private Long priceSlab;
    private String checkIn;
    private String checkOut;
    private Integer adults;
    private Integer children;
    private String amount;
    private String status;
    private List<LocationDto> location;
}


