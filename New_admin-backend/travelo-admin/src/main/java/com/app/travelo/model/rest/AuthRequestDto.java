package com.app.travelo.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto implements Serializable {
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String countryCode;
    private String otp;
    private Long roleId;

    private String hotelName;

    private String contactPerson;

    private String whatsappNumber;

    private String contactNumber;

    private String designation;

}
