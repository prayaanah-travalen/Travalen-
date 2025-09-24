package com.app.travelo.controller;

import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.model.rest.AuthRequestDto;
import com.app.travelo.model.rest.AuthResponseDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.UserDto;
import com.app.travelo.repositories.UserRepository;
import com.app.travelo.services.authentication.AuthenticationService;
import com.app.travelo.services.authentication.UserDetailsService;
import com.app.travelo.services.email.OtpService;
import com.app.travelo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private OtpService otpService;
    
    @Autowired
    private UserRepository userRepo;

    @RequestMapping({ "hello" })
    public String firstPage() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return "Hello World";
    }


    @PostMapping(value = "/auth/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authenticationRequest){
        AuthResponseDto returnMap = null;
        try{
            String emailId = authService.verifyLogin(authenticationRequest);
            if(Objects.nonNull(emailId)){
                String jwtToken = createAuthenticationToken(authenticationRequest);
                returnMap = toAuthResponseDto("SUCCESS","Login Success", jwtToken);

            }else{
                returnMap = toAuthResponseDto("FAILED","Incorrect username/password", "");
            }

        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED","Something went wrong", "");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK); 
    }

    @PostMapping(value = "/auth/register")
    public ResponseEntity<AuthResponseDto> userRegister(@RequestBody AuthRequestDto req){
        AuthResponseDto returnMap = null;
        try{
            String res = authService.userRegister(req);
            returnMap = toAuthResponseDto("SUCCESS",res, "");
        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED",e.getMessage(), "");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/sendOtp")
    public ResponseEntity<AuthResponseDto> sendOtp(@RequestBody AuthRequestDto req){
        AuthResponseDto returnMap = null;
        try{
            String res = authService.sendOtp(req);
            returnMap = toAuthResponseDto("SUCCESS",res, "");
        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED",e.getMessage(), "");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/forgotPass/verifyOtp")
    public ResponseEntity<AuthResponseDto> verifyOtForgotPass(@RequestBody AuthRequestDto authenticationRequest){
        AuthResponseDto returnMap = null;
        try{
            //verify otp
            if(authenticationRequest.getOtp().equals(otpService.getCacheOtp(authenticationRequest.getEmailId()))){
                returnMap = toAuthResponseDto("SUCCESS","Otp verified successfully","");
                otpService.clearOtp(authenticationRequest.getEmailId());
            }else{
                returnMap = toAuthResponseDto("FAILED","Otp is either expired or incorrect","");
            }

        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED",e.getMessage(),"");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }

    //create auth token
    public String createAuthenticationToken(AuthRequestDto authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmailId(), "", authService.getAuthorities(authenticationRequest.getEmailId()))
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmailId());

        return jwtTokenUtil.generateToken(userDetails, authService.getUserDetail(authenticationRequest.getEmailId()));
    }

    @PostMapping(value = "/auth/verifyOtp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody AuthRequestDto authenticationRequest){
        AuthResponseDto returnMap = null;
        try{
            //verify otp
            if(authenticationRequest.getOtp().equals(otpService.getCacheOtp(authenticationRequest.getEmailId()))){
                authService.saveNewUser(authenticationRequest);
                String jwtToken = createAuthenticationToken(authenticationRequest);
                returnMap = toAuthResponseDto("SUCCESS","Otp verified successfully", jwtToken);
                otpService.clearOtp(authenticationRequest.getEmailId());
            }else{
                returnMap = toAuthResponseDto("FAILED","Otp is either expired or incorrect","");
            }

        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED",e.getMessage(),"");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }
    
//    @PostMapping(value = "/auth/verifyOtp")
//    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody AuthRequestDto authenticationRequest) {
//        AuthResponseDto returnMap = null;
//        try {
//            
//            if (authenticationRequest.getOtp().equals(otpService.getCacheOtp(authenticationRequest.getEmailId()))) {
//
//                authService.saveNewUser(authenticationRequest);
//
//                
//                String jwtToken = createAuthenticationToken(authenticationRequest);
//
//                
//                UserEntity user = userRepo.getUser(authenticationRequest.getEmailId());
//                Long hotelCode = null;
//                String hotelName = null;
//                if (user.getHotels() != null && !user.getHotels().isEmpty()) {
//                    HotelEntity hotel = user.getHotels().iterator().next();
//                    hotelCode = hotel.getHotelCode();
//                    hotelName = hotel.getHotelName();
//                }
//
//             
//                returnMap = AuthResponseDto.builder()
//                        .status("SUCCESS")
//                        .message("OTP verified successfully")
//                        .jwt(jwtToken)
//                        .hotelCode(hotelCode)
//                        .hotelName(hotelName)
//                        .build();
//
//                otpService.clearOtp(authenticationRequest.getEmailId());
//            } else {
//                returnMap = AuthResponseDto.builder()
//                        .status("FAILED")
//                        .message("OTP is either expired or incorrect")
//                        .jwt("")
//                        .hotelCode(null)
//                        .hotelName(null)
//                        .build();
//            }
//        } catch (Exception e) {
//            returnMap = AuthResponseDto.builder()
//                    .status("FAILED")
//                    .message(e.getMessage())
//                    .jwt("")
//                    .hotelCode(null)
//                    .hotelName(null)
//                    .build();
//        }
//
//        return new ResponseEntity<>(returnMap, HttpStatus.OK);
//    }


    @PostMapping(value = "/auth/changePassword")
    public ResponseEntity<AuthResponseDto> changePassword(@RequestBody AuthRequestDto authenticationRequest){
        AuthResponseDto returnMap = null;
        try{
            //verify otp
            authService.changePassword(authenticationRequest);
            String jwtToken = createAuthenticationToken(authenticationRequest);
            returnMap = toAuthResponseDto("SUCCESS","Otp verified successfully", jwtToken);
            otpService.clearOtp(authenticationRequest.getEmailId());


        } catch (Exception e){
            returnMap = toAuthResponseDto("FAILED",e.getMessage(),"");
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }


    private static AuthResponseDto toAuthResponseDto(String status, String message, String jwt) {
        return AuthResponseDto.builder()
                .jwt(jwt)
                .status(status)
                .message(message)
                .build();

    }


}
