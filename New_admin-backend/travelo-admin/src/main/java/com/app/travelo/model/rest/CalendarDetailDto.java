package com.app.travelo.model.rest;

import lombok.*;@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor

public class CalendarDetailDto {
    private String status;
    private Integer roomsToSell;
    private int netBooked;
    private String standardRate;
    private String nonRefundableRate;
    private String weeklyRate;
    private String roomName;
    private Long roomId;
}
