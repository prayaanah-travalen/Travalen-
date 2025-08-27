package com.app.travelo.model.rest;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CalendarResDto {
    private String roomName;
    private List<CalendarDetailDto> data;
}
