package com.app.travelo.repositories;

import com.app.travelo.model.entity.HotelAmenityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelAmenityRepository extends JpaRepository<HotelAmenityEntity,Long> {

    @Query("select ha from HotelAmenityEntity ha where ha.hotelCode.hotelCode=:hotelCode")
    List<HotelAmenityEntity> findByHotelCode(Long hotelCode);
}
