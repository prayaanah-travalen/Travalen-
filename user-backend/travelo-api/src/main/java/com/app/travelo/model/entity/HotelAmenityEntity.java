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
@Table(name = "hotel_amenity")
public class HotelAmenityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "amenity")
    private String amenity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_code")
    private HotelEntity hotelCode;

}
