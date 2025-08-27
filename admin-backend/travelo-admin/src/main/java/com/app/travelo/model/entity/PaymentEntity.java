package com.app.travelo.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="payment_id")
    private String paymentId;

    @Column(name="parent_payment_id")
    private String parentPaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookingEntity bookingId;

    @Column(name="transaction_no")
    private String transactionNo;

    @Column(name="transaction_type")
    private String transactionType;

    @Column(name="status")
    private String status;

    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="payment_type")
    private String paymentType;

    @Column(name="guest_id")
    private Long guestId;

    @Column(name="registration_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDateTime;

    @Column(name="registrant_by")
    private String registrantBy;

    @Column(name="last_updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDateTime;

    @Column(name="last_updated_by")
    private String lastUpdatedBy;

    @Column(name="bank_id")
    private String bankId;

    @Column(name="auth_transaction_id")
    private String authTransactionId;

    @Column(name="pg_transaction_id")
    private String pgTransactionId;

    @Column(name="pg_service_transaction_id")
    private String pgServiceTransactionId;

    @Column(name="amount")
    private String amount;

    @Column(name="merchant_transaction_id")
    private String merchantTransactionId;
}
