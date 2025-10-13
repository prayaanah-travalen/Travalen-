package com.app.travelo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JsonIgnoreProperties({"amenities", "hibernateLazyInitializer", "handler"})
    private HotelEntity hotelCode;

}
