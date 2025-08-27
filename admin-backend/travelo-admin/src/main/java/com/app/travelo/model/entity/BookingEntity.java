package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "booking")
public class   BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id")
    private Long bookingId;

    @Column(name="no_of_hotels")
    private String noOfHotels;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "guest_id")
    private GuestEntity guestDetail;

    @Column(name="check_in")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkIn;

    @Column(name="check_out")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOut;

    @Column(name="no_of_children")
    private Integer children;

    @Column(name="no_of_adults")
    private Integer adults;

    @Column(name="amount")
    private String amount;

    @Column(name="status")
    private String status;

    @OneToMany(mappedBy = "bookingId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<BookingDetailsEntity> bookingDetails;

    @OneToMany(mappedBy = "bookingId")
    private List<PaymentEntity> payment;

    @Column(name="registration_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDateTime;

    @Column(name="registrant_by", updatable = false)
    private String registrantBy;

    @Column(name="last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDateTime;

    @Column(name="last_updated_by")
    private String lastUpdatedBy;

}
