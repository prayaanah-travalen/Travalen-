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
@Table(name = "hotel")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_code")
    private Long hotelCode;

    @Column(name = "hotel_name")
    private String hotelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private  String postalCode;

    @Column(name = "city")
    private  String city;

    @Column(name = "state")
    private  String state;

    @Column(name = "country")
    private String country;

    @Column(name = "star_rating")
    private String starRating;

    @OneToMany(mappedBy = "hotelCode")
    private List<HotelImageEntity> hotelImages;

    @OneToMany(mappedBy = "hotelCode")
    private List<HotelRoomEntity> rooms;


    @Column(name= "property_rule")
    private String propertyRule;

    @Column(name= "website_link")
    private String websiteLink;

    @Column(name= "about")
    private String about;

    @OneToMany(mappedBy = "hotelCode", cascade = CascadeType.ALL)
    private List<HotelAmenityEntity> amenities;

    @Column(name = "email")
    private String email;

    @Column(name= "availability")
    private Boolean availability;

}
