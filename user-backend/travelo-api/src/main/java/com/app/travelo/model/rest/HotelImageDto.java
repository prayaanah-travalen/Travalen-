package com.app.travelo.model.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelImageDto {
    private Long imageId;
    private byte[] image;
}
