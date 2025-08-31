package com.app.travelo.repositories;

import com.app.travelo.model.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    @Query("SELECT bk FROM BookingEntity bk where bk.guestDetail.guestId=:guestId ")
    List<BookingEntity> getUserBooking(@Param("guestId") Long guestId);

    @Query(value = "SELECT * FROM booking bk " +
            "INNER JOIN booking_details bd ON bk.booking_id = bd.booking_id " +
            "INNER JOIN hotel_room hr ON bd.hotel_room_id = hr.hotel_room_id " +
            "INNER JOIN users_hotel uh ON hr.hotel_code= uh.hotel_code " +
            "where uh.user_id=:userId", nativeQuery = true)
    List<BookingEntity> getBookings(@Param("userId") Long userId);

//    List<BookingEntity> findAllByOrderByIdAsc();

    @Query("SELECT bk FROM BookingEntity bk where bk.registrationDateTime between :start and :end ")
    List<BookingEntity> getUserBookingWithDate(@Param("start") Date start, @Param("end") Date end);

    @Query(value = "SELECT * FROM booking bk " +
            "INNER JOIN booking_details bd ON bk.booking_id = bd.booking_id " +
            "where bd.hotel_room_id = :roomId AND bk.status = :status", nativeQuery = true)
    List<BookingEntity> getBookingsForRoomId(@Param("roomId") Long roomId, @Param("status") String status);

}
