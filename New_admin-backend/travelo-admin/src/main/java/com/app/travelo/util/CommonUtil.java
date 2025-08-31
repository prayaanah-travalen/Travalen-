package com.app.travelo.util;

import com.app.travelo.model.entity.GuestEntity;
import com.app.travelo.model.entity.UserEntity;
import com.app.travelo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommonUtil {

    @Autowired
    private UserRepository userRepo;


    public static String getLoginUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

    public static List<String> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> userDetails = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();
        return userDetails.stream().map(rl->rl.getAuthority()).collect(Collectors.toList());

    }

    public String  getUserId() {
        String userName = getLoginUserName();
        if (Objects.nonNull(userName)) {
            UserEntity userEntity = userRepo.getUser(userName);
            return userEntity.getUserId().toString();
        }
        return "SYSTEM";
    }
}
