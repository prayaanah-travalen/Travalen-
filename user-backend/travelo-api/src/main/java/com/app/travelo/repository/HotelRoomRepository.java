package com.app.travelo.repository;

import com.app.travelo.model.entity.HotelRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoomEntity, Long> {
}
