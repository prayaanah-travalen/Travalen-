package com.app.travelo.services.authentication.impl;

import com.app.travelo.model.common.EmailDetailDto;
import com.app.travelo.model.entity.HotelEntity;
import com.app.travelo.model.entity.LocationEntity;
import com.app.travelo.model.entity.RoleEntity;
import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.model.enums.UserType;
import com.app.travelo.model.rest.*;
import com.app.travelo.repositories.HotelRepository;
import com.app.travelo.repositories.LocationRepository;
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
import org.springframework.transaction.annotation.Transactional;

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
    
    @Autowired
    private LocationRepository locationRepo;


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
            // Check if user is already active
            if ("active".equals(user.getStatus())) {
                return "User already exists and is active. Please sign in instead.";
            }
            // If user exists but isn't active, allow OTP resend
        }
        
        // Generate OTP
        String otp = otpService.generateOtp(req.getEmailId());
        
        // Prepare email content with HTML formatting
        String body = "<html>Dear "+ req.getFirstName() +",<br><br>  Your OTP is " + otp + 
                     ". Use this OTP to register for Travalen Partner Application. This OTP is valid for only 5 minutes. <br><br> Regards, <br> Team Travalen</html>";
        
        EmailDetailDto emailDto = EmailDetailDto.builder()
                .subject("OTP verification for Travalen Application")
                .msgBody(body)
                .recipient(req.getEmailId())
                .build();

        // Send email with error handling
        try {
            emailService.sendEmailWithHtmlTemplate(emailDto);
            return "OTP sent to your email";
        } catch (Exception e) {
            // Clear OTP if email fails
            otpService.clearOtp(req.getEmailId());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public String saveNewUser(AuthRequestDto req) {
        try {
            UserEntity user = userRepo.getUser(req.getEmailId());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Set<RoleEntity> roles = new HashSet<>();
            RoleEntity roleEntity = roleRepo.findByRoleName("Hotel Admin");
            roles.add(roleEntity);
            
            HotelEntity savedHotel;
            
            if(Objects.isNull(user)) {
                // Create a default location if needed
                LocationEntity defaultLocation = locationRepo.findByLocation("TBD");
                if (Objects.isNull(defaultLocation)) {
                    defaultLocation = locationRepo.save(LocationEntity.builder().location("TBD").build());
                }
                
                // Create hotel with all required fields
                HotelEntity newHotel = HotelEntity.builder()
                    .hotelName(req.getHotelName())
                    .location(defaultLocation)
                    .address("TBD") // Default address to be updated later
                    .email(req.getEmailId()) // Use the user's email
                    .availability(true)
                    .active(true)
                    .build();
                
                // Save hotel first to get the hotel code
                savedHotel = hotelRepo.save(newHotel);
                
                // Refresh the hotel entity to ensure we have the latest data
                savedHotel = hotelRepo.findById(savedHotel.getHotelCode()).orElse(savedHotel);
                
                Set<HotelEntity> hotel = new HashSet<>();
                hotel.add(savedHotel);
                
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
                // Update existing user
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
                user.setContactNumber(req.getContactNumber());
                user.setContactPerson(req.getContactPerson());
                user.setDesignation(req.getDesignation());
                user.setWhatsappNumber(req.getWhatsappNumber());
                user.setHotelName(req.getHotelName());
                
                // If the user doesn't have a hotel yet, create one
                if (user.getHotels() == null || user.getHotels().isEmpty()) {
                    LocationEntity defaultLocation = locationRepo.findByLocation("TBD");
                    if (Objects.isNull(defaultLocation)) {
                        defaultLocation = locationRepo.save(LocationEntity.builder().location("TBD").build());
                    }
                    
                    HotelEntity newHotel = HotelEntity.builder()
                        .hotelName(req.getHotelName())
                        .location(defaultLocation)
                        .address("TBD")
                        .email(req.getEmailId())
                        .availability(true)
                        .active(true)
                        .build();
                        
                    // Save hotel first to get the hotel code
                    savedHotel = hotelRepo.save(newHotel);
                    
                    // Refresh the hotel entity to ensure we have the latest data
                    savedHotel = hotelRepo.findById(savedHotel.getHotelCode()).orElse(savedHotel);
                    
                    Set<HotelEntity> hotel = new HashSet<>();
                    hotel.add(savedHotel);
                    user.setHotels(hotel);
                } else {
                    // Use existing hotel
                    savedHotel = user.getHotels().iterator().next();
                    // Refresh the hotel entity to ensure we have the latest data
                    savedHotel = hotelRepo.findById(savedHotel.getHotelCode()).orElse(savedHotel);
                }
            }

            UserEntity savedUser = userRepo.save(user);
            
            // Ensure hotel code is not null
            if (savedHotel.getHotelCode() == null) {
                throw new RuntimeException("Hotel code is null after saving");
            }
            
            // Return the hotel code for frontend use
            return "success:" + savedHotel.getHotelCode();
        }
        catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
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
            userRepo.save(user); // Added save operation
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