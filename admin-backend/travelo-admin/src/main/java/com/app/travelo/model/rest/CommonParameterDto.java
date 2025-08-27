package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CommonParameterDto {
    private String parameterId;
    private String parameter;
    private String description;
}
