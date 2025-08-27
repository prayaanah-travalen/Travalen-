package com.app.travelo.service.payment.impl;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.entity.PaymentEntity;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.enums.PaymentStatus;
import com.app.travelo.model.rest.EmailDetailDto;
import com.app.travelo.model.rest.payment.*;
import com.app.travelo.model.rest.payment.callback.CallBackRequest;
import com.app.travelo.model.rest.payment.callback.CallBackResponse;
import com.app.travelo.repository.PaymentRepository;
import com.app.travelo.service.booking.BookingHelper;
import com.app.travelo.service.email.EmailService;
import com.app.travelo.service.payment.PaymentService;
import com.app.travelo.util.PhonePeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service(value = "PhonePePaymentServiceImpl")
public class PhonePePaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepo;

    @Value("${phonePe.payLink.url}")
    private String phonePePayLinkUrl;

    @Value("${phonePe.status.url}")
    private String phonePeStatusUrl;

    @Value("${phonePe.saltKey}")
    private String phonePeSaltKey;

    @Value("${phonePe.saltIndex}")
    private String phonePeSaltIndex;

    @Value("${phonePe.redirectUrl}")
    private String phonePeRedirectUrl;

    @Value("${phonePe.callBackUrl}")
    private String phonePeCallBackUrl;

    @Value("${phonePe.merchantId}")
    private String phonePeMerchantId;

    @Value("${phonePe.merchantUserId}")
    private String phonePeMerchantUserId;

    @Value("${phonePe.refund.url}")
    private String refundUrl;

    @Value("${url.myTrip}")
    private String myTripUrl;

    @Autowired
    private BookingHelper bookingHelper;

    @Autowired
    private EmailService emailService;

    @Override
    public PaymentDto getPaymentId() {

        String paymentId = generatePaymentId();
//        PaymentEntity paymentEntity = PaymentEntity.builder()
//                .paymentId(paymentId)
//                .build();
//        paymentRepo.save(paymentEntity);
        return PaymentDto.builder()
                .paymentId(paymentId)
                .build();
    }

    private String generatePaymentId() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String transactionFormat = "TR%s-%09d";
        String randomNumber = new DecimalFormat("000000000000")
                .format(new Random().nextInt(999999999));
        return String.format(transactionFormat, localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), Integer.parseInt(randomNumber));

    }

    @Override
    public PaymentResponseDto getPaymentLink(PaymentEntity paymentEntity, String amount, String phone) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            PhonePeRequestDto req = PhonePeRequestDto.builder()
                    .merchantId(phonePeMerchantId)
                    .merchantTransactionId(paymentEntity.getPaymentId())
                    .merchantUserId(phonePeMerchantUserId)
                    .amount(amount)
                    .redirectUrl(phonePeRedirectUrl)
                    .redirectMode("REDIRECT")
                    .callbackUrl(phonePeCallBackUrl)
                    .mobileNumber(phone)
                    .paymentInstrument(PhonePeRequestDto.toPaymentInstrument("PAY_PAGE"))
                    .build();

            ObjectMapper objMapper = new ObjectMapper();
            String jsonStr;

            jsonStr = objMapper.writeValueAsString(req);


            String encodedString = PhonePeUtil.encodeBase64(jsonStr);

            String checksum = PhonePeUtil.generateCheckSum(jsonStr, "/pg/v1/pay", phonePeSaltKey, Integer.parseInt(phonePeSaltIndex));

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            header.set("X-Verify", checksum);

            HttpEntity<PhonePeRequest> request = new HttpEntity<>(PhonePeRequest.builder()
                    .request(encodedString)
                    .build()
                    , header);

            PhonePeResponseDto response = restTemplate
                    .postForObject(phonePePayLinkUrl, request, PhonePeResponseDto.class);

            return PaymentResponseDto.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .paymentLink(response.getData().getInstrumentResponse().getRedirectInfo().getUrl())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            paymentEntity.setStatus("ERROR");
            paymentRepo.save(paymentEntity);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
        return null;
    }




    @Override
    public PaymentDto getStatus(CallBackRequest req) {
//        PaymentEntity paymentEntity = paymentRepo.getPaymentByPaymentId(id);
//        PaymentDto paymentDto = null;
//        if (Objects.nonNull(paymentEntity)) {
//            paymentDto = getStatusFromPhonePe(paymentEntity);
//            if(paymentDto.getStatus().equals(PaymentStatus.SUCCESS.name())){
//                String message = "<html>Dear" + paymentEntity.getBookingId().getGuestDetail().getFirstName()
//                        + "<br><br>Hotel booking("+ paymentEntity.getBookingId().getBookingId() +") was completed."
//                        + " Please visit <a href=\""+myTripUrl+"\">My Trips</a> for further details"
//                        + "<br><br>Regards,<br> Team Travalen</html>";
//                EmailDetailDto emailDto = EmailDetailDto.builder()
//                        .subject("Booking successful")
//                        .msgBody( "Dear Customer, booking is completed")
//                        .recipient(paymentEntity.getBookingId().getGuestDetail().getEmail())
//                        .build();
////                emailService.sendSimpleMail(emailDto);
//                emailService.sendEmailWithHtmlTemplate(emailDto);
//            }
//
//        }
//        return paymentDto;
        return null;
    }

    private PaymentDto getStatusFromPhonePe(PaymentEntity paymentEntity) {
        PaymentStatus paymentStatus = null;
        BookingStatus bookingStatus = null;

        PaymentDto paymentDto = null;
        RestTemplate restTemplate = new RestTemplate();
        String checkSumBody = "/pg/v1/status/" + phonePeMerchantId + "/" + paymentEntity.getPaymentId();
        String checksum = PhonePeUtil.getStatusChecksumHeader(checkSumBody, phonePeSaltKey, Integer.parseInt(phonePeSaltIndex));
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Verify", checksum);
        header.set("X-MERCHANT-ID", phonePeMerchantId);

        HttpEntity<String> request = new HttpEntity<>(header);
        String url = phonePeStatusUrl.concat(phonePeMerchantId).concat("/").concat(paymentEntity.getPaymentId());
        ResponseEntity<PhonePeResponseDto> response = restTemplate
                .exchange(url, HttpMethod.GET, request, PhonePeResponseDto.class);

        if (response.getBody().getSuccess()) {
            if(response.getBody().getCode().equals("PAYMENT_SUCCESS")) {
                paymentStatus = PaymentStatus.SUCCESS;
                bookingStatus = BookingStatus.BOOKED;
            } else if(response.getBody().getCode().equals("PAYMENT_PENDING")) {
                paymentStatus = PaymentStatus.PENDING;
                bookingStatus = BookingStatus.PENDING;
            }

        } else {
            paymentStatus = PaymentStatus.ERROR;
            bookingStatus = BookingStatus.FAILED;
        }
        bookingHelper.updateBookingStatus(paymentEntity.getBookingId().getBookingId(), bookingStatus);
        paymentEntity.setStatus(paymentStatus.name());
        paymentEntity.setLastUpdatedDateTime(new Date());
        paymentEntity.setLastUpdatedBy("SYSTEM");
        paymentRepo.save(paymentEntity);

        paymentDto = PaymentDto.builder()
                .paymentId(paymentEntity.getPaymentId())
                .status(bookingStatus.name())
                .build();

        return paymentDto;
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
                    .guest(booking.getGuestDetail())
                    .registrationDateTime(new Date())
                    .build();
            paymentRepo.save(refundEntity);

            executeRefund(refundEntity, paymentEntity);

        }
    }

    @Override
    public PaymentDto getStatusOfBooking(Long id) {
        PaymentEntity paymentEntity = paymentRepo.getPaymentByBookingId(id);
        PaymentDto paymentDto = null;
        if (Objects.nonNull(paymentEntity)) {
            paymentDto = getStatusFromPhonePe(paymentEntity);
        }
        return paymentDto;
    }

    private Boolean executeRefund(PaymentEntity refundEntity, PaymentEntity originalPaymentEntity) {
        RefundReqDto refundReq = RefundReqDto.builder()
                .amount(refundEntity.getAmount())
                .callbackUrl(phonePeCallBackUrl)
                .merchantId(phonePeMerchantId)
                .merchantTransactionId(refundEntity.getPaymentId())
                .originalTransactionId(originalPaymentEntity.getPaymentId())
                .merchantUserId(phonePeMerchantUserId)
                .build();

        ObjectMapper objMapper = new ObjectMapper();
        String jsonStr;
        try {
            jsonStr = objMapper.writeValueAsString(refundReq);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String encodedString = PhonePeUtil.encodeBase64(jsonStr);

        String checksum = PhonePeUtil.generateCheckSum(jsonStr, "/pg/v1/refund", phonePeSaltKey, Integer.parseInt(phonePeSaltIndex));

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("X-Verify", checksum);

        HttpEntity<PhonePeRequest> request = new HttpEntity<>(PhonePeRequest.builder()
                .request(encodedString)
                .build()
                , header);

        RestTemplate restTemplate = new RestTemplate();
        PhonePeResponseDto response = restTemplate
                .postForObject(refundUrl, request, PhonePeResponseDto.class);
//
//        return PayLinkResponseDto.builder()
//                .paymentId(paymentId)
//                .paymentLink(response.getData().getInstrumentResponse().getRedirectInfo().getUrl())
//                .build();
        if (response.getSuccess()) {
            return true;
        }

        return false;

    }
}
