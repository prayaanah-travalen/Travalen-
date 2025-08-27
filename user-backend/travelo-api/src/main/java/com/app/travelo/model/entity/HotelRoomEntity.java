package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "hotel_room")
public class HotelRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_room_id")
    private Long hotelRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_code")
    private HotelEntity hotelCode;

   @Column(name = "room_name")
    private String roomName;

    @Column(name = "occupancy")
    private Integer occupancy;

    @Column(name = "room_description")
    private String roomDescription;

    @Column(name = "price")
    private String price;

    @Column(name = "bed_type")
    private String bedType;

    @OneToMany(mappedBy = "hotelRoomId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoomImageEntity> roomImages;

    @OneToMany(mappedBy = "hotelRoomId", cascade = CascadeType.ALL)
    private List<PriceSlabRoomsEntity> priceSlab;

    @OneToMany(mappedBy = "hotelRoomId", cascade = CascadeType.ALL)
    private List<RoomAmenityEntity> amenities;

    @OneToMany(mappedBy = "hotelRoomId", cascade = CascadeType.ALL)
    private List<RoomTagEntity> roomTags;

    @Column(name="no_of_rooms")
    private Integer noOfRooms;
}
