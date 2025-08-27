package com.app.travelo.util;

import com.app.travelo.model.entity.GuestEntity;
import com.app.travelo.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    @Autowired
    private static GuestRepository guestRepo;

    public static String getLoginUserPhone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    public static Long  getUserId(){
        GuestEntity guestEntity = guestRepo.findByPhone(getLoginUserPhone());
        return  guestEntity.getGuestId();
    }
}
