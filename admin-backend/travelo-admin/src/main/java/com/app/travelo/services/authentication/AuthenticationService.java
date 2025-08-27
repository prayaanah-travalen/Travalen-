package com.app.travelo.services.authentication;

import com.app.travelo.model.rest.AuthRequestDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthenticationService {
    String verifyLogin(AuthRequestDto req);

    List<GrantedAuthority> getAuthorities(String email);

    String userRegister(AuthRequestDto req);

    String saveNewUser(AuthRequestDto req);

    String sendOtp(AuthRequestDto req);

    String changePassword(AuthRequestDto req);

    UserDto getUserDetail(String email);

}
