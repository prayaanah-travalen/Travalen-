package com.app.travelo.repository;

import com.app.travelo.model.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    @Query("SELECT bk FROM BookingEntity bk where bk.guestDetail.guestId=:guestId ")
    List<BookingEntity> getUserBooking(@Param("guestId") Long guestId);

    @Query("SELECT bk FROM BookingEntity bk where bk.guestDetail.guestId=:guestId AND bk.status= :status ")
    BookingEntity getUserBookingWithStatus(@Param("guestId") Long guestId, @Param("status") String status);

    @Query(value = "SELECT * FROM booking bk " +
            "INNER JOIN booking_details bd ON bk.booking_id = bd.booking_id " +
            "where bd.hotel_room_id = :roomId AND bk.status = :status", nativeQuery = true)
    List<BookingEntity> getBookingsForRoomId(@Param("roomId") Long roomId, @Param("status") String status);

}
