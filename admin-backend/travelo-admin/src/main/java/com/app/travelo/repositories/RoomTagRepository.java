package com.app.travelo.repositories;

import com.app.travelo.model.entity.HotelAmenityEntity;
import com.app.travelo.model.entity.RoomTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTagRepository extends JpaRepository<RoomTagEntity, Long> {

    @Query("select rt from RoomTagEntity rt where rt.hotelRoomId.hotelRoomId=:roomCode")
    List<RoomTagEntity> findByRoomCode(Long roomCode);
}
