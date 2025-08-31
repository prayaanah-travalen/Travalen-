package com.app.travelo.controller;

import com.app.travelo.model.rest.PaymentDto;
import com.app.travelo.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("status")
    public ResponseEntity<PaymentDto> getStatus(@RequestParam("id") Long id) {
        return  new ResponseEntity<>(paymentService.getStatus(id), HttpStatus.OK);
    }
}

