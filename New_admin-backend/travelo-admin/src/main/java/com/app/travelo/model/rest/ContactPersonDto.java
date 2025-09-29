package com.app.travelo.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPersonDto {

	 private Long id;
	    private Long hotelCode;
	    private String firstName;
	    private String lastName;
	    private String phone;
	    private String whatsapp;
	    private String email;
	    private String designation;
}
