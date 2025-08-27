package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class HotelImageDto {
    private Long imageId;
    private byte[] image;
    private String imageName;
}
