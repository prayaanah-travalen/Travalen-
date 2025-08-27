package com.app.travelo.services.authentication;

import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationService authService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepo.getUser(userName);
        if(user==null){
           throw new UsernameNotFoundException("User not found");
       }

        return new org.springframework.security.core.userdetails.User(user.getEmailId(), "",
                authService.getAuthorities(user.getEmailId()));
    }
}