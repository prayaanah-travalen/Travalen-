package com.app.travelo.services.authentication.impl;

import com.app.travelo.model.common.EmailDetailDto;
import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.entity.RoleEntity;
import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.model.enums.UserType;
import com.app.travelo.model.rest.*;
import com.app.travelo.repositories.HotelRepository;
import com.app.travelo.repositories.RoleRepository;
import com.app.travelo.repositories.UserRepository;
import com.app.travelo.services.authentication.AuthenticationService;
import com.app.travelo.services.email.EmailService;
import com.app.travelo.services.email.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private HotelRepository hotelRepo;


    @Override
    public String verifyLogin(AuthRequestDto req) {
        UserEntity user = userRepo.getUser(req.getEmailId());
        if(Objects.nonNull(user)) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean validPassword = passwordEncoder.matches(req.getPassword(), user.getPassword());
            if (validPassword) {
                return user.getEmailId();
            }
        }
        throw new UsernameNotFoundException("User not found");

    }

    @Override
    public List<GrantedAuthority> getAuthorities(String emailId) {
        UserEntity user = userRepo.getUser(emailId);
        return user.getRoles().stream().map(this::setSimpleGrand).collect(Collectors.toList());

    }

    @Override
    public String userRegister(AuthRequestDto req) {
        UserEntity user = userRepo.getUser(req.getEmailId());
        if(Objects.nonNull(user)) {
            return "User already exist";
        }
        String body =  "<html>Dear "+ req.getFirstName() +",<br><br>  Your OTP is " + otpService.generateOtp(req.getEmailId()) + ". Use this otp to log in to Travalen Partner Application. This otp is valid for only 5 minutes. <br><br> Regards, <br> Team Travalen</html>";
        EmailDetailDto emailDto = EmailDetailDto.builder()
                .subject("OTP verification for Travalen Application")
                .msgBody(body)
                .recipient(req.getEmailId())
                .build();


        emailService.sendEmailWithHtmlTemplate(emailDto);
//        emailService.sendSimpleMail(emailDto);
        return "Email send";
    }

    @Override
    public String saveNewUser(AuthRequestDto req) {
        try {
            UserEntity user = userRepo.getUser(req.getEmailId());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Set<RoleEntity> roles = new HashSet<>();
            RoleEntity roleEntity = roleRepo.findByRoleName("Hotel Admin");
            roles.add(roleEntity);
            if(Objects.isNull(user)) {
                Set<HotelEntity> hotel = new HashSet<>();

                hotel.add( HotelEntity.builder().hotelName(req.getHotelName()).active(false).build());
                

                user = UserEntity.builder()
                        .userName(req.getFirstName())
                        .emailId(req.getEmailId())
                        .phone(req.getPhoneNo())
                        .password(passwordEncoder.encode(req.getPassword()))
                        .roles(roles)
                        .countryCode(req.getCountryCode())
                        .userType(UserType.EXTERNAL.name())
                        .firstName(req.getFirstName())
                        .lastName(req.getLastName())
                        .status("active")
                        .contactNumber(req.getContactNumber())
                        .contactPerson(req.getContactPerson())
                        .designation(req.getDesignation())
                        .whatsappNumber(req.getWhatsappNumber())
                        .hotelName(req.getHotelName())
                        .hotels(hotel)
                        .build();
            } else {
                user.setUserName(req.getFirstName());
                user.setEmailId(req.getEmailId());
                user.setPhone(req.getPhoneNo());
                user.setPassword(passwordEncoder.encode(req.getPassword()));
                user.setRoles(roles);
                user.setCountryCode(req.getCountryCode());
                user.setUserType(UserType.EXTERNAL.name());
                user.setFirstName(req.getFirstName());
                user.setLastName(req.getLastName());
                user.setStatus("active");
            }

            userRepo.save(user);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    @Override
    public String sendOtp(AuthRequestDto req) {
        UserEntity user = userRepo.getUser(req.getEmailId());
        if(Objects.isNull(user)) {
            return "User not exist";
        }
        EmailDetailDto emailDto = EmailDetailDto.builder()
                .subject("OTP verification for Travalen Application")
                .msgBody( "Dear Customer , Your OTP is " + otpService.generateOtp(req.getEmailId()) + ". Use this otp to log in to Travalen Partner Application. This otp is valid for only 5 minutes")
                .recipient(req.getEmailId())
                .build();
        emailService.sendSimpleMail(emailDto);
        return "Email send";
    }

    @Override
    public String changePassword(AuthRequestDto req) {
        UserEntity user = userRepo.getUser(req.getEmailId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(Objects.nonNull(user)) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        } else {
            return "User not exist";
        }
        return "Password updated";
    }


    private Set<GrantedAuthority> setGrantedAuthority(List<RoleEntity> roles) {

        return roles.stream().map(this::setSimpleGrand).collect(Collectors.toSet());
    }

    private SimpleGrantedAuthority setSimpleGrand(RoleEntity role) {
        return new SimpleGrantedAuthority(role.getRoleName());
    }



    @Override
    public UserDto getUserDetail(String email) {
        UserEntity usr = userRepo.getUser(email);
        List<HotelEntity> hotels =  hotelRepo.getHotels(usr.getUserId());

        UserDto user = UserDto.builder()
                .userId(usr.getUserId())
                .emailId(usr.getEmailId())
                .phoneNo(usr.getPhone())
                .countryCode(usr.getCountryCode())
                .lastName(usr.getLastName())
                .firstName(usr.getFirstName())
                .role(usr.getRoles().stream().map(rol-> RoleDto.builder().roleId(rol.getRoleId()).roleName(rol.getRoleName()).build()).collect(Collectors.toSet()))
                .userType(Objects.nonNull(usr.getUserType()) ? UserType.valueOf(usr.getUserType()) : null)
                .status(Objects.nonNull(usr.getStatus()) ? usr.getStatus().toUpperCase() : null)

                .build();
        if(!hotels.isEmpty()){
            user.setHotelCode(hotels.get(0).getHotelCode());
            user.setHotelName(hotels.get(0).getHotelName());
        }
        return user;

    }
}
