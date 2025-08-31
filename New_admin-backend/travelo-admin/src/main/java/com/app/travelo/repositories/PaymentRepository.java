package com.app.travelo.repositories;

import com.app.travelo.model.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT pe FROM PaymentEntity pe where pe.bookingId.bookingId=:bookingId")
    List<PaymentEntity> getPaymentListByBookingId(@Param("bookingId") Long bookingId);


    @Query("SELECT pe FROM PaymentEntity pe where pe.paymentId=:paymentId")
    PaymentEntity getPaymentByPaymentId(@Param("paymentId") String paymentId);

    @Query("SELECT pe FROM PaymentEntity pe where pe.bookingId.bookingId=:bookingId")
    PaymentEntity getPaymentByBookingId(@Param("bookingId") Long bookingId);

}
