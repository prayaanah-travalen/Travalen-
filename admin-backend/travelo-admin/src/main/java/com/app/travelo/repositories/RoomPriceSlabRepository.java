package com.app.travelo.repositories;

import com.app.travelo.model.entity.PriceSlabRoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomPriceSlabRepository  extends JpaRepository<PriceSlabRoomsEntity, Long> {

    @Query("select rp from PriceSlabRoomsEntity rp where rp.hotelRoomId.hotelRoomId=:roomCode")
    List<PriceSlabRoomsEntity> findByRoomId(Long roomCode);
}
