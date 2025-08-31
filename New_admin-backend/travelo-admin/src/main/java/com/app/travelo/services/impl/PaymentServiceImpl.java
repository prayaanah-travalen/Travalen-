//package com.app.travelo.services.impl;
//
//import com.app.travelo.model.entity.BookingEntity;
//import com.app.travelo.model.entity.PaymentEntity;
//import com.app.travelo.model.enums.PaymentStatus;
//import com.app.travelo.model.rest.PaymentDto;
//import com.app.travelo.model.rest.payment.PhonePeRequest;
//import com.app.travelo.model.rest.payment.PhonePeResponseDto;
//import com.app.travelo.model.rest.payment.RefundReqDto;
//import com.app.travelo.repositories.PaymentRepository;
//import com.app.travelo.services.PaymentService;
//import com.app.travelo.util.PhonePeUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.text.DecimalFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.Random;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//    @Autowired
//    private PaymentRepository paymentRepo;
//
//
//    @Value("${phonePe.saltKey}")
//    private String phonePeSaltKey;
//
//    @Value("${phonePe.saltIndex}")
//    private String phonePeSaltIndex;
//
//    @Value("${phonePe.redirectUrl}")
//    private String phonePeRedirectUrl;
//
//    @Value("${phonePe.callBackUrl}")
//    private String phonePeCallBackUrl;
//
//    @Value("${phonePe.merchantId}")
//    private String phonePeMerchantId;
//
//    @Value("${phonePe.merchantUserId}")
//    private String phonePeMerchantUserId;
//
//    @Value("${phonePe.refund.url}")
//    private String refundUrl;
//
//    @Override
//    public PaymentDto getStatus(Long id) {
//        List<PaymentEntity> paymentEntity = paymentRepo.getPaymentListByBookingId(id);
//        PaymentDto paymentDto = null;
//        if(Objects.nonNull(paymentEntity)) {
////            paymentDto = PaymentDto.builder()
////                    .paymentId(paymentEntity.getPaymentId())
////                    .status(paymentEntity.getStatus())
////                    .build();
//        }
//        return paymentDto;
//    }
//
//
//    @Override
//    @Transactional
//    public void refund(BookingEntity booking) {
//        PaymentEntity paymentEntity = paymentRepo.getPaymentByBookingId(booking.getBookingId());
//        if (Objects.nonNull(paymentEntity)) {
//            PaymentEntity refundEntity = PaymentEntity.builder()
//                    .paymentId(generatePaymentId())
//                    .parentPaymentId(paymentEntity.getPaymentId())
//                    .status(PaymentStatus.INITIAL.name())
//                    .transactionType(PaymentStatus.REFUND.name())
//                    .amount(booking.getAmount())
//                    .bookingId(booking)
//                    .guestId(booking.getGuestDetail().getGuestId())
//                    .registrationDateTime(new Date())
//                    .build();
//            paymentRepo.save(refundEntity);
//
//            executeRefund(refundEntity, paymentEntity);
//
//        }
//    }
//
//    private Boolean executeRefund(PaymentEntity refundEntity, PaymentEntity originalPaymentEntity) {
//        RefundReqDto refundReq = RefundReqDto.builder()
//                .amount(refundEntity.getAmount())
//                .callbackUrl(phonePeCallBackUrl)
//                .merchantId(phonePeMerchantId)
//                .merchantTransactionId(refundEntity.getPaymentId())
//                .originalTransactionId(originalPaymentEntity.getPaymentId())
//                .merchantUserId(phonePeMerchantUserId)
//                .build();
//
//        ObjectMapper objMapper = new ObjectMapper();
//        String jsonStr;
//        try {
//            jsonStr = objMapper.writeValueAsString(refundReq);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        String encodedString = PhonePeUtil.encodeBase64(jsonStr);
//
//        String checksum = PhonePeUtil.generateCheckSum(jsonStr, "/pg/v1/refund", phonePeSaltKey, Integer.parseInt(phonePeSaltIndex));
//
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//        header.set("X-Verify", checksum);
//
//        HttpEntity<PhonePeRequest> request = new HttpEntity<>(PhonePeRequest.builder()
//                .request(encodedString)
//                .build()
//                , header);
//
//        RestTemplate restTemplate = new RestTemplate();
//        PhonePeResponseDto response = restTemplate
//                .postForObject(refundUrl, request, PhonePeResponseDto.class);
////
////        return PayLinkResponseDto.builder()
////                .paymentId(paymentId)
////                .paymentLink(response.getData().getInstrumentResponse().getRedirectInfo().getUrl())
////                .build();
//        if (response.getSuccess()) {
//            return true;
//        }
//
//        return false;
//
//    }
//
//    private String generatePaymentId() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        String transactionFormat = "TR%s-%09d";
//        String randomNumber = new DecimalFormat("000000000000")
//                .format(new Random().nextInt(999999999));
//        return String.format(transactionFormat, localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), Integer.parseInt(randomNumber));
//
//    }
//
//}
