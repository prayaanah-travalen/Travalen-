package com.app.travelo.repository;

import com.app.travelo.model.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT pe FROM PaymentEntity pe where pe.bookingId.bookingId=:bookingId")
    PaymentEntity getPaymentByBookingId(@Param("bookingId") Long bookingId);


    @Query("SELECT pe FROM PaymentEntity pe where pe.paymentId=:paymentId")
    PaymentEntity getPaymentByPaymentId(@Param("paymentId") String paymentId);

    @Query("SELECT pe FROM PaymentEntity pe where pe.orderId=:orderId")
    PaymentEntity getPaymentByOrderId(@Param("orderId") String orderId);
}
