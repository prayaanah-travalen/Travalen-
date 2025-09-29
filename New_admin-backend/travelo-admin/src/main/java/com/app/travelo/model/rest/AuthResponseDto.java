package com.app.travelo.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String status;
    private String message;
    private String jwt;
    
    private Long hotelCode;  
   private String hotelName;  
}
