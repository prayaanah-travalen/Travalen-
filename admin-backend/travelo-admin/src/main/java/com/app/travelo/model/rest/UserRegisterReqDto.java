package com.app.travelo.model.rest;

import lombok.Data;

@Data
public class UserRegisterReqDto {
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNo;

}
