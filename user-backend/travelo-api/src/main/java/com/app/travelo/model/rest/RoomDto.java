package com.app.travelo.model.rest;

import com.app.travelo.model.enums.BedTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RoomDto {
    private Long roomCode;
    private String roomName;
    private BedTypeEnum bedType;
}
