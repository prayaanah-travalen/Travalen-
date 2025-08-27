package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PopularDestDto {
    private Long popularDestId;
    private String location;
    private String image;
    private String url;


}