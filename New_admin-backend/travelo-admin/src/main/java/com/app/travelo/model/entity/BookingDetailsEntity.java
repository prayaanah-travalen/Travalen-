package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "booking_details")
public class BookingDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_detail_id")
    private Long bookingDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookingEntity bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_room_id")
    private HotelRoomEntity hotelRoomId;

    @Column(name="no_of_rooms")
    private Integer noOfRooms;

    @Column(name="check_in")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkIn;

    @Column(name="check_out")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOut;
}
