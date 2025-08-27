package com.app.travelo.repositories;

import com.app.travelo.model.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query(value = "SELECT htl.* FROM hotel htl " +
            "INNER JOIN users_hotel uh ON htl.hotel_code= uh.hotel_code " +
            "where uh.user_id=:userId and htl.availability = true", nativeQuery = true)
    List<HotelEntity> getHotels(@Param("userId") Long userId);

    @Query(value = "SELECT htl.* FROM hotel htl " +
            "where htl.hotel_code= :hotelCode and htl.availability = true", nativeQuery = true)
    HotelEntity getHotelsById(@Param("hotelCode") Long hotelCode);

    @Query(value = "SELECT htl.* FROM hotel htl " +
            "where htl.availability = true", nativeQuery = true)
    List<HotelEntity> getAllHotels();

}
