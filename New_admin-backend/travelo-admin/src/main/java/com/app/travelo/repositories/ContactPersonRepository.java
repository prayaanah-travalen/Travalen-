package com.app.travelo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.travelo.model.entity.ContactPersonEntity;

public interface ContactPersonRepository extends JpaRepository<ContactPersonEntity, Long> {
	
	@Query("SELECT cp FROM ContactPersonEntity cp WHERE cp.hotel.hotelCode = :hotelCode")
    List<ContactPersonEntity> findByHotelCode(@Param("hotelCode") Long hotelCode);

}
