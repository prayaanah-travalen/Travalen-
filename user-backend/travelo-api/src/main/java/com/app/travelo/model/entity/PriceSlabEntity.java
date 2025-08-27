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
@Table(name = "price_slab")
public class PriceSlabEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "price_slab")
    private Integer priceSlab;

    @Column(name="max_allowed_guest")
    private Integer maxAllowedGuest;

    @Column(name="max_allowed_room")
    private Integer maxAllowedRoom;

    @Column(name="no_of_nights")
    private Integer noOfNights;
}
