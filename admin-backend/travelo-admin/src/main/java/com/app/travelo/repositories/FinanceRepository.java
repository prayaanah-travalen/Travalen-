package com.app.travelo.repositories;

import com.app.travelo.model.entity.FinanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends JpaRepository<FinanceEntity, Long> {

    @Query("SELECT fin FROM FinanceEntity fin where fin.hotel.hotelCode=:hotelCode")
    FinanceEntity getHotelFinance(@Param("hotelCode") Long hotelCode);
}
