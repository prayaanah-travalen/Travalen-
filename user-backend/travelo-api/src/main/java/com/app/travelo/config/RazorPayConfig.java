package com.app.travelo.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorPayConfig {


    @Value("${razorPay.key}")
    private String razorPayKey;

    @Value("${razorPay.keySecret}")
    private String razorPayKeySecret;

    public RazorpayClient razorpayClient() {
        try {
            return new RazorpayClient(razorPayKey, razorPayKeySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }
}
