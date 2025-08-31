package com.app.travelo.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.entity.PaymentEntity;
import com.app.travelo.model.enums.PaymentStatus;
import com.app.travelo.model.rest.PaymentDto;
import com.app.travelo.repositories.PaymentRepository;
import com.app.travelo.services.PaymentService;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.json.JSONObject;
import com.razorpay.Refund;


@Service
public class RazorpayPaymentServiceImpl implements PaymentService{
	
	@Autowired
    private PaymentRepository paymentRepo;
	
	@Value("${razorpay.keyId}")
    private String razorpayKeyId;

    @Value("${razorpay.secret}")
    private String razorpaySecret;
    
    private RazorpayClient getClient() throws Exception {
        return new RazorpayClient(razorpayKeyId, razorpaySecret);
    }
    
    @Override
    public PaymentDto getStatus(Long id) {
        List<PaymentEntity> paymentEntities = paymentRepo.getPaymentListByBookingId(id);
        if (Objects.nonNull(paymentEntities) && !paymentEntities.isEmpty()) {
            PaymentEntity paymentEntity = paymentEntities.get(0);

            try {
                RazorpayClient client = getClient();
                Payment payment = client.payments.fetch(paymentEntity.getPaymentId());

                String status = payment.get("status");  // Razorpay status is like "created", "captured", "failed"

                // Map Razorpay status to your enum
                String mappedStatus;
                switch (status.toLowerCase()) {
                    case "captured":
                        mappedStatus = PaymentStatus.SUCCESS.name();
                        break;
                    case "failed":
                        mappedStatus = PaymentStatus.ERROR.name();
                        break;
                    default:
                        mappedStatus = PaymentStatus.INITIAL.name();
                        break;
                }

                return PaymentDto.builder()
                        .paymentId(payment.get("id"))
                        .status(mappedStatus)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Error fetching payment status", e);
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void refund(BookingEntity booking) {
        PaymentEntity paymentEntity = paymentRepo.getPaymentByBookingId(booking.getBookingId());
        if (Objects.nonNull(paymentEntity)) {
            PaymentEntity refundEntity = PaymentEntity.builder()
                    .paymentId(generatePaymentId())
                    .parentPaymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.INITIAL.name())
                    .transactionType(PaymentStatus.REFUND.name())
                    .amount(booking.getAmount())
                    .bookingId(booking)
                    .guestId(booking.getGuestDetail().getGuestId())
                    .registrationDateTime(new Date())
                    .build();
            paymentRepo.save(refundEntity);

            executeRefund(refundEntity, paymentEntity);
        }
    }

    private Boolean executeRefund(PaymentEntity refundEntity, PaymentEntity originalPaymentEntity) {
        try {
            RazorpayClient client = getClient();

            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", Integer.parseInt(refundEntity.getAmount()) * 100); // amount in paise

            Refund refund = client.payments.refund(originalPaymentEntity.getPaymentId(), refundRequest);

            if ("processed".equalsIgnoreCase(refund.get("status"))) {
                refundEntity.setStatus(PaymentStatus.SUCCESS.name());
                paymentRepo.save(refundEntity);
                return true;
            } else {
                refundEntity.setStatus(PaymentStatus.ERROR.name());   
                paymentRepo.save(refundEntity);
                return false;
            }
        } catch (Exception e) {
            refundEntity.setStatus(PaymentStatus.ERROR.name());   
            paymentRepo.save(refundEntity);
            throw new RuntimeException("Refund failed", e);
        }
    }


    private String generatePaymentId() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String transactionFormat = "TR%s-%09d";
        String randomNumber = new DecimalFormat("000000000000")
                .format(new Random().nextInt(999999999));
        return String.format(transactionFormat,
                localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                Integer.parseInt(randomNumber));
    }
    
    
}
