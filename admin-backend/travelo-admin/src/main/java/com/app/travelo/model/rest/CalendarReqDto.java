package com.app.travelo.model.rest;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CalendarReqDto {
    private String startDate;
    private String endDate;
}
