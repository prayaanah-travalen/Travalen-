package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_code")
    private Long roomCode;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "bed_type")
    private String bedType;

}
