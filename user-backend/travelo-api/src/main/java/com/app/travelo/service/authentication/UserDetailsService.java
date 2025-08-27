package com.app.travelo.service.authentication;

import com.app.travelo.model.entity.GuestEntity;
import com.app.travelo.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private GuestRepository guestRepos;
    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        GuestEntity guest = guestRepos.findByPhone(phoneNo);
        if(guest==null){
           GuestEntity guestEntity = GuestEntity.builder().phone(phoneNo).build();
           guest = guestRepos.save(guestEntity);

       }

        return new org.springframework.security.core.userdetails.User(phoneNo, "",
                new ArrayList<>());
    }
}