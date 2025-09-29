package com.app.travelo.repositories;

import com.app.travelo.model.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

//    @Query(value = "SELECT htl.* FROM hotel htl " +
//            "INNER JOIN users_hotel uh ON htl.hotel_code= uh.hotel_code " +
//            "where uh.user_id=:userId and htl.availability = true", nativeQuery = true)
//    List<HotelEntity> getHotels(@Param("userId") Long userId);

//    @Query(value = "SELECT htl.* FROM hotel htl " +
//            "where htl.hotel_code= :hotelCode and htl.availability = true", nativeQuery = true)
//    HotelEntity getHotelsById(@Param("hotelCode") Long hotelCode);
    
    @Query(value = "SELECT htl.* FROM hotel htl " +
            "where htl.hotel_code= :hotelCode", nativeQuery = true)
    HotelEntity getHotelsByIdForUpdate(@Param("hotelCode") Long hotelCode);

    @Query(value = "SELECT htl.* FROM hotel htl " +
            "where htl.availability = true", nativeQuery = true)
    List<HotelEntity> getAllHotels();
    
    @Query(value = "SELECT htl.* FROM hotel htl " +
            "INNER JOIN users_hotel uh ON htl.hotel_code= uh.hotel_code " +
            "where uh.user_id=:userId", nativeQuery = true) // Removed availability condition
    List<HotelEntity> getHotels(@Param("userId") Long userId);
    
    

//    @Query(value = "SELECT htl.hotel_code, htl.hotel_name, htl.city, htl.state, htl.email, " +
//            "htl.star_rating, htl.availability, htl.active, loc.location_id, loc.location " +
//            "FROM hotel htl INNER JOIN location loc ON htl.location_id = loc.location_id " +
//            "WHERE htl.availability = true", nativeQuery = true)
//    List<Object[]> getAllHotelsBasic();
//
//    @Query(value = "SELECT htl.hotel_code, htl.hotel_name, htl.city, htl.state, htl.email, " +
//            "htl.star_rating, htl.availability, htl.active, loc.location_id, loc.location " +
//            "FROM hotel htl INNER JOIN location loc ON htl.location_id = loc.location_id " +
//            "INNER JOIN users_hotel uh ON htl.hotel_code = uh.hotel_code " +
//            "WHERE uh.user_id = :userId", nativeQuery = true)
//    List<Object[]> getHotelsByUserBasic(@Param("userId") Long userId);
//    
    
   

}
