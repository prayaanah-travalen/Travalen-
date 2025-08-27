package com.app.travelo.repository;

import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.rest.HotelSearchRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    @Query("select htl from HotelEntity htl " +
            "inner join HotelRoomEntity htlrm on htl.hotelCode = htlrm.hotelCode " +
            "inner join PriceSlabRoomsEntity psr on htlrm.hotelRoomId = psr.hotelRoomId " +
            "where psr.priceSlabId.priceSlab <= :priceSlabs " +
            "and htl.location.locationId IN (:locations) " +
            "and htl.availability = true")
    List<HotelEntity> getHotels(@Param("locations") List<Long> locations,
                                @Param("priceSlabs") Integer priceSlabs);

    @Query("select htl from HotelEntity htl " +
            "left join HotelRoomEntity htlrm on htl.hotelCode = htlrm.hotelCode " +
            "where htl.location.locationId IN (:locations) " +
            "and htl.availability = true")
    List<HotelEntity> getHotels(@Param("locations") List<Long> locations);

}
