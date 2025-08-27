package com.app.travelo.service.payment.impl;

import com.app.travelo.config.RazorPayConfig;
import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.entity.PaymentEntity;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.enums.PaymentStatus;
import com.app.travelo.model.rest.EmailDetailDto;
import com.app.travelo.model.rest.payment.*;
import com.app.travelo.model.rest.payment.callback.CallBackRequest;
import com.app.travelo.repository.PaymentRepository;
import com.app.travelo.service.booking.BookingHelper;
import com.app.travelo.service.email.EmailService;
import com.app.travelo.service.payment.PaymentService;
import com.app.travelo.util.PhonePeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import org.json.JSONException;
import org.json.JSONObject;
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

@Service(value = "PaymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {
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

    @Autowired
    private RazorPayConfig razorPay;

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
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto responseDto = new PaymentResponseDto();
        try {

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", Double.parseDouble(paymentRequestDto.getAmount())*100);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", paymentRequestDto.getPaymentId());
            JSONObject notes = new JSONObject();
            notes.put("notes_key_1",paymentRequestDto.getBookingId().getBookingId());
            orderRequest.put("notes",notes);

            Order order = razorPay.razorpayClient().orders.create(orderRequest);

            paymentRequestDto.setOrderId(order.get("id"));

            PaymentEntity paymentEntity = savePaymentDetails(paymentRequestDto);

            responseDto.setPaymentId(paymentEntity.getPaymentId());
            responseDto.setOrderId(order.get("id"));

        } catch (RazorpayException | JSONException e) {
            throw new RuntimeException(e);
        }
        return responseDto;

    }

    @Override
    public PaymentDto getStatus(CallBackRequest req) {
        PaymentEntity paymentEntity = paymentRepo.getPaymentByOrderId(req.getOrderId());
        PaymentDto paymentDto = null;
        if (Objects.nonNull(paymentEntity)) {

            paymentEntity.setTransactionNo(req.getMerchantPaymentId());

            paymentDto = updatePaymentStatus(paymentEntity);

            if(paymentDto.getStatus().equals(BookingStatus.BOOKED.name())){
                String message = "<html>Dear" + paymentEntity.getBookingId().getGuestDetail().getFirstName()
                        + "<br><br>Hotel booking("+ paymentEntity.getBookingId().getBookingId() +") was completed."
                        + " Please visit <a href=\""+myTripUrl+"\">My Trips</a> for further details"
                        + "<br><br>Regards,<br> Team Travalen</html>";
                EmailDetailDto emailDto = EmailDetailDto.builder()
                        .subject("Booking successful")
                        .msgBody( message)
                        .recipient(paymentEntity.getBookingId().getGuestDetail().getEmail())
                        .build();
//                emailService.sendSimpleMail(emailDto);
                emailService.sendEmailWithHtmlTemplate(emailDto);
            }
          
        }
        return paymentDto;
    }

    private PaymentDto updatePaymentStatus(PaymentEntity paymentEntity) {
        PaymentStatus paymentStatus = null;
        BookingStatus bookingStatus = null;

        PaymentDto paymentDto = null;

        try {
           Payment payment= razorPay.razorpayClient().payments.fetch(paymentEntity.getTransactionNo());
            if (Objects.nonNull(payment) && Objects.nonNull(payment.get("status"))) {
                if(payment.get("status").toString().toUpperCase().equals(PaymentStatus.CAPTURED.name())) {
                    paymentStatus = PaymentStatus.SUCCESS;
                    bookingStatus = BookingStatus.BOOKED;
                } else  {
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
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

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
                    .transactionNo(paymentEntity.getTransactionNo())
                    .build();

            executeRefund(refundEntity, paymentEntity);

            paymentRepo.save(refundEntity);

        }
    }

    @Override
    public PaymentDto getStatusOfBooking(Long id) {
        PaymentEntity paymentEntity = paymentRepo.getPaymentByBookingId(id);
        PaymentDto paymentDto = null;
        if (Objects.nonNull(paymentEntity)) {
            paymentDto = updatePaymentStatus(paymentEntity);
        }
        return paymentDto;
    }

    private void executeRefund(PaymentEntity refundEntity, PaymentEntity originalPaymentEntity) {

        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount",refundEntity.getAmount());
            refundRequest.put("speed","normal");
            JSONObject notes = new JSONObject();
            notes.put("merchant_payment_id",refundEntity.getPaymentId());
            notes.put("booking_id",refundEntity.getBookingId().getBookingId());
            refundRequest.put("notes",notes);
            refundRequest.put("receipt",refundEntity.getPaymentId());

            Refund refund = razorPay.razorpayClient().payments.refund(refundEntity.getTransactionNo(),refundRequest);

            refundEntity.setStatus(refund.get("status"));


        } catch (JSONException | RazorpayException e) {
            throw new RuntimeException(e);
        }

//
//        return PayLinkResponseDto.builder()
//                .paymentId(paymentId)
//                .paymentLink(response.getData().getInstrumentResponse().getRedirectInfo().getUrl())
//                .build();



    }

    private PaymentEntity savePaymentDetails(PaymentRequestDto request) {
        String paymentId = getPaymentId().getPaymentId();
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentId)
                .status(PaymentStatus.INITIAL.name())
                .transactionType("AUTHORISATION")
                .amount(request.getAmount())
                .bookingId(request.getBookingId())
                .guest(request.getGuest())
                .registrationDateTime(new Date())
                .orderId(request.getOrderId())
                .build();

        return paymentRepo.save(paymentEntity);
    }
}
