package com.app.travelo.controller;

import com.app.travelo.service.authentication.AuthenticationService;
import com.app.travelo.util.JwtUtil;
import com.app.travelo.model.rest.AuthRequestDto;
import com.app.travelo.service.authentication.OtpService;
import com.app.travelo.service.authentication.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    private OtpService otpService;

    @Autowired
    private AuthenticationService authService;


    @RequestMapping({ "hello" })
    public String firstPage() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return "Hello World";
    }

    @PostMapping(value = "/auth/sendOtp")
    public ResponseEntity<Map<String,Object>> getOtp(@RequestBody AuthRequestDto req){
        Map<String,Object> returnMap=new HashMap<>();
        try{
            //generate OTP
            String otp = otpService.generateOtp(req.getPhoneNo());
            returnMap.put("status","success");
            returnMap.put("message","Otp sent successfully");
        }catch (Exception e){
 e.printStackTrace();

            returnMap.put("status","failed");
            returnMap.put("message",e.getMessage());
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/verifyOtp")
    public ResponseEntity<Map<String,Object>> verifyOtp(@RequestBody AuthRequestDto authenticationRequest){
        Map<String,Object> returnMap=new HashMap<>();
        try{
            //verify otp
            if(authenticationRequest.getOtp().equals(otpService.getCacheOtp(authenticationRequest.getPhoneNo()))){
                String jwtToken = createAuthenticationToken(authenticationRequest);
                returnMap.put("status","success");
                returnMap.put("message","Otp verified successfully");
                returnMap.put("jwt",jwtToken);
                otpService.clearOtp(authenticationRequest.getPhoneNo());
            }else{
                returnMap.put("status","failed");
                returnMap.put("message","Otp is either expired or incorrect");
            }

        } catch (Exception e){
            returnMap.put("status","failed");
            returnMap.put("message",e.getMessage());
        }

        return new ResponseEntity<>(returnMap, HttpStatus.OK); 
    }

    //create auth token
    public String createAuthenticationToken(AuthRequestDto authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getPhoneNo(), "")
            );
            authService.saveUserDetails(authenticationRequest);
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getPhoneNo());
        return jwtTokenUtil.generateToken(userDetails);
    }

    @GetMapping("/auth/logout")
    public void logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    }
}
