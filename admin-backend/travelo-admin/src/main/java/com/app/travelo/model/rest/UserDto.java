package com.app.travelo.model.rest;

import com.app.travelo.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long userId;
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String countryCode;
    private Set<RoleDto> role;
    private UserType userType;
    private String status;
    private String hotelName;
    private Long hotelCode;

}
