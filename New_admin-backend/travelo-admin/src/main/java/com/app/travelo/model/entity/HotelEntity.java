package com.app.travelo.model.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder

@AllArgsConstructor
//@EqualsAndHashCode
@EqualsAndHashCode(exclude = {"finance"}) 
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

    @Column(name = "email")
    private String email;


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

    @Column(name= "about")
    private String about;

    @Column(name= "property_rule")
    private String propertyRule;

    @Column(name= "website_link")
    private String websiteLink;

    @Column(name= "latitude")
    private String latitude;

    @Column(name= "longitude")
    private String longitude;

    @Column(name= "availability")
    private Boolean availability;

//    @Column(name= "availability", insertable = false, updatable = false)
//    private Boolean active;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
   
    
    
    @OneToMany(mappedBy = "hotelCode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelImageEntity> hotelImages;

    @OneToMany(mappedBy = "hotelCode" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelRoomEntity> rooms;

    @OneToMany(mappedBy = "hotelCode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelAmenityEntity> amenities;

//    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "users_hotel",
//            joinColumns = { @JoinColumn(name = "hotel_code") },
//            inverseJoinColumns = { @JoinColumn(name = "user_id") }
//    )
//    Set< UserEntity > user = new HashSet< UserEntity>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_hotel",
            joinColumns = { @JoinColumn(name = "hotel_code") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    Set<UserEntity> user = new HashSet<>();

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private FinanceEntity finance;
    
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactPersonEntity> contactPersons;

}
