package com.app.travelo.repositories;

import com.app.travelo.model.entity.BookingDetailsEntity;
import com.app.travelo.model.entity.HotelRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetailsEntity, Long> {

//    @Query("SELECT bk FROM BookingDetailsEntity bd INNER JOIN Booking")
    List<BookingDetailsEntity> findByHotelRoomId(HotelRoomEntity hotelRoomId);
}
