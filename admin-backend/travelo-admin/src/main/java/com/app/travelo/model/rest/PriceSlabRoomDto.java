package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PriceSlabRoomDto {
    private Long id;

    private PriceSlabDto priceSlabId;

    private Long hotelRoomId;

    private Long hotelCode;
}
