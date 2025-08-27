package com.app.travelo.services.impl;

import com.app.travelo.model.common.EmailDetailDto;
import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.entity.RoleEntity;
import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.model.entity.UserRoleEntity;
import com.app.travelo.model.enums.UserStatus;
import com.app.travelo.model.enums.UserType;
import com.app.travelo.model.rest.HotelDto;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.RoleDto;
import com.app.travelo.model.rest.UserDto;
import com.app.travelo.repositories.RoleRepository;
import com.app.travelo.repositories.UserRepository;
import com.app.travelo.repositories.UserRoleRepository;
import com.app.travelo.services.HotelService;
import com.app.travelo.services.UserManagementService;
import com.app.travelo.services.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private HotelService hotelService;


    @Override
    public ResponseDto<String> saveInternalUser(UserDto req) {
        ResponseDto<String> response = new ResponseDto<>();
        try {

            UserEntity user = new UserEntity();
            UserEntity existingUser = userRepo.getUser(req.getEmailId());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            Set<RoleEntity> roles = new HashSet<>();
            Set<RoleEntity> roles = roleRepo.findAllById(req.getRole().stream().map(rol->rol.getRoleId()).collect(Collectors.toSet()))
                    .stream().collect(Collectors.toSet());

            if(Objects.nonNull(existingUser)) {
                response.setErrorCode("400");
                response.setErrorMessage("User alreday exist");
                return response;
            }
            String password = generatePassword();

            user.setUserName(req.getFirstName());
            user.setEmailId(req.getEmailId());
            user.setPhone(req.getPhoneNo());
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(roles);
            user.setCountryCode(req.getCountryCode());
            user.setUserType(Objects.nonNull(req.getUserType()) ? req.getUserType().name() : UserType.INTERNAL.name());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setStatus("active");

            userRepo.save(user);
            response.setSuccess("SUCCESS");

            EmailDetailDto emailDto = EmailDetailDto.builder()
                    .subject("Account created for Travalen Partner Application")
                    .msgBody( "Dear Customer , Please find the password to login to the Travalen partner application.  " + password )
                    .recipient(req.getEmailId())
                    .build();
            emailService.sendSimpleMail(emailDto);
        }
        catch (Exception e) {
            log.debug(e.getMessage());
            response.setErrorCode("500");
            response.setErrorMessage("Something went wrong");
        }

        return response;
    }

    @Override
    public ResponseDto<List<UserDto>> getInternalUsers() {
        ResponseDto<List<UserDto>> response = new ResponseDto<>();
        try{
            List<UserEntity> users = userRepo.findUserByUserType(UserType.INTERNAL.name());
            List<UserDto> userDto = users.stream().map(this::toUserDtoList).collect(Collectors.toList());
            response.setResponse(userDto);
            response.setSuccess("SUCCESS");
        } catch (Exception e) {
            response.setErrorCode("500");
            response.setErrorMessage("Something went wrong");
        }

        return response;
    }

    @Override
    public ResponseDto<List<UserDto>> getExternalUsers() {
        ResponseDto<List<UserDto>> response = new ResponseDto<>();
        try{
            List<UserEntity> users = userRepo.findUserByUserType(UserType.EXTERNAL.name());
            List<UserDto> userDto = users.stream().map(this::toUserDtoList).collect(Collectors.toList());
            response.setResponse(userDto);
            response.setSuccess("SUCCESS");
        } catch (Exception e) {
            response.setErrorCode("500");
            response.setErrorMessage("Something went wrong");
        }

        return response;
    }

    private UserDto toUserDtoList(UserEntity usr) {
        return UserDto.builder()
                .userId(usr.getUserId())
                .emailId(usr.getEmailId())
                .phoneNo(usr.getPhone())
                .countryCode(usr.getCountryCode())
                .lastName(usr.getLastName())
                .firstName(usr.getFirstName())
                .role(usr.getRoles().stream().map(rol-> RoleDto.builder().roleId(rol.getRoleId()).roleName(rol.getRoleName()).build()).collect(Collectors.toSet()))
                .userType(UserType.valueOf(usr.getUserType()))
                .status(usr.getStatus().toUpperCase())
                .build();
    }

    @Override
    public ResponseDto<String> updateUserStatus(UserDto req, UserStatus status) {
        ResponseDto<String> response = new ResponseDto<>();
        try{
            Optional<UserEntity> existingUser = userRepo.findById(req.getUserId());
            if(existingUser.isPresent()) {
                UserEntity user = existingUser.get();
                user.setStatus(status.name());
                userRepo.save(user);
                response.setSuccess("SUCCESS");
            } else {
                response.setErrorMessage("User not found");
                response.setErrorCode("400");
            }

        } catch (Exception e) {
            response.setErrorMessage("Something went wrong");
            response.setErrorCode("500");
        }

        return response;
    }

    @Override
    public ResponseDto<List<RoleDto>> getRoles() {
        ResponseDto<List<RoleDto>> response = new ResponseDto<>();
        List<RoleEntity> roles = roleRepo.findAll();
        List<RoleDto> rolesDtos = roles.stream().map(rol->RoleDto.builder()
                .roleName(rol.getRoleName())
                .roleId(rol.getRoleId())
                .build()).collect(Collectors.toList());
        response.setResponse(rolesDtos);
        response.setSuccess("SUCCESS");
        return response;
    }

    @Override
    @Transactional
    public ResponseDto<String> updateUser(UserDto req) {
        ResponseDto<String> response = new ResponseDto<>();
        try {

            UserEntity user = userRepo.getUser(req.getEmailId());

           if(Objects.isNull(user)) {
                response.setErrorCode("404");
                response.setErrorMessage("User not found");
                return response;
            }

            Set<RoleEntity> roles = roleRepo.findAllById(req.getRole().stream().map(rol->rol.getRoleId()).collect(Collectors.toSet()))
                    .stream().collect(Collectors.toSet());

            user.setUserName(req.getFirstName());
            user.setEmailId(req.getEmailId());
            user.setPhone(req.getPhoneNo());
            user.setRoles(roles);
            user.setCountryCode(req.getCountryCode());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());

            userRepo.save(user);
            response.setSuccess("SUCCESS");

        }
        catch (Exception e) {
            log.debug(e.getMessage());
            response.setErrorCode("500");
            response.setErrorMessage("Something went wrong");
        }

        return response;
    }

    @Override
    public ResponseDto<UserDto> getUserDetail(UserDto user) {
        ResponseDto<UserDto> response = new ResponseDto<>();
        UserEntity usr = userRepo.getUser(user.getEmailId());
        List<HotelDto> hotels =  hotelService.getHotels();
        UserDto userDto = UserDto.builder()
                .userId(usr.getUserId())
                .emailId(usr.getEmailId())
                .phoneNo(usr.getPhone())
                .countryCode(usr.getCountryCode())
                .lastName(usr.getLastName())
                .firstName(usr.getFirstName())
                .role(usr.getRoles().stream().map(rol-> RoleDto.builder().roleId(rol.getRoleId()).roleName(rol.getRoleName()).build()).collect(Collectors.toSet()))
                .userType(UserType.valueOf(usr.getUserType()))
                .status(usr.getStatus().toUpperCase())
                .hotelCode(hotels.get(0).getHotelCode())
                .hotelName(hotels.get(0).getHotelName())
                .build();
        response.setResponse(userDto);
        return response;
    }

    private static String generatePassword() {
        String AB = "!#0123456789#ABCDEFGHIJKLMNOPQRSTU$VWXYZ";
        Random rnd = new Random();
        String specialChar = "@#!";
        StringBuilder sb = new StringBuilder(10);
        sb.append(specialChar.charAt(rnd.nextInt(specialChar.length())));
        for (int i = 0; i < 10; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
