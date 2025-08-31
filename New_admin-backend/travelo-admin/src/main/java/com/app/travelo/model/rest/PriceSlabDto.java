package com.app.travelo.model.rest;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor

public class PriceSlabDto {
    private Long id;

    private Integer priceSlab;

    private Integer maxAllowedGuest;

    private Integer maxAllowedRoom;

    private Integer noOfNights;
}
