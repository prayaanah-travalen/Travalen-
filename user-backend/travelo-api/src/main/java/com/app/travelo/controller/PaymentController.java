package com.app.travelo.controller;

import com.app.travelo.model.rest.payment.PaymentDto;
import com.app.travelo.model.rest.payment.callback.CallBackRequest;
import com.app.travelo.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    @Qualifier("PaymentServiceImpl")
    private PaymentService paymentService;

    @GetMapping("/payment_id")
    public ResponseEntity<PaymentDto> getPaymentId() {
        return  new ResponseEntity<>(paymentService.getPaymentId(), HttpStatus.OK);
    }

    @PostMapping("status")
    public ResponseEntity<PaymentDto> getStatus(@RequestBody CallBackRequest req) {
        return  new ResponseEntity<>(paymentService.getStatus(req), HttpStatus.OK);
    }

}

