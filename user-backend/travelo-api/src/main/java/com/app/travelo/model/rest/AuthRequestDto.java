package com.app.travelo.model.rest;

import lombok.Data;

import java.io.Serializable;
@Data
public class AuthRequestDto implements Serializable {
    private String otp;
    private String phoneNo;
    private String firstName;
    private String lastName;
}
