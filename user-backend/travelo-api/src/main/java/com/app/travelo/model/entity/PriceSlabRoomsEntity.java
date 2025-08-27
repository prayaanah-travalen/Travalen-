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
@Table(name = "rooms_price_slab")
public class PriceSlabRoomsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="price_slab_id")
    private PriceSlabEntity priceSlabId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_room_id")
    private HotelRoomEntity hotelRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_code")
    private HotelEntity hotelCode;
}
