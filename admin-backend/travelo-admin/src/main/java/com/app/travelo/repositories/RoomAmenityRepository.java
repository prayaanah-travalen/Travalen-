package com.app.travelo.repositories;

import com.app.travelo.model.entity.RoomAmenityEntity;
import com.app.travelo.model.entity.RoomTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenityEntity, Long> {

    @Query("select rt from RoomAmenityEntity rt where rt.hotelRoomId.hotelRoomId=:roomCode")
    List<RoomAmenityEntity> findByRoomCode(Long roomCode);
}
