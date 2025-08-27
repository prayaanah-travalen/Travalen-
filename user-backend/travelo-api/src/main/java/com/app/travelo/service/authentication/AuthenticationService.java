package com.app.travelo.service.authentication;

import com.app.travelo.model.entity.GuestEntity;
import com.app.travelo.model.rest.AuthRequestDto;
import com.app.travelo.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private GuestRepository guestRepos;

    public void saveUserDetails(AuthRequestDto req) {
        GuestEntity guest = guestRepos.findByPhone(req.getPhoneNo());

        if(guest==null){
            guest = GuestEntity.builder()
                    .phone(req.getPhoneNo())
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .build();
         } else {
            guest.setFirstName(req.getFirstName());
            guest.setLastName(req.getLastName());
        }

        guestRepos.save(guest);
    }
}
