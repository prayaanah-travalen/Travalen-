package com.app.travelo.model.entity;

import lombok.*;

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
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    private Set<RoleEntity> role;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    Set < RoleEntity > roles = new HashSet< RoleEntity >();

    @Column(name="user_name")
    private String userName;

    @Column(name="email_id")
    private String emailId;

    @Column(name="phone")
    private String phone;

    @Column(name="country_code")
    private String countryCode;

    @Column(name="password")
    private String password;

    @ManyToMany(mappedBy = "user", cascade = { CascadeType.ALL })
    private Set<HotelEntity> hotels;

    @Column(name="user_type")
    private String userType;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="status")
    private String status;

    @Column(name="hotel_name")
    private String hotelName;

    @Column(name="contact_person")
    private String contactPerson;


    @Column(name="whatsapp_number")
    private String whatsappNumber;

    @Column(name="contact_number")
    private String contactNumber;

    @Column(name="designation")
    private String designation;
}
