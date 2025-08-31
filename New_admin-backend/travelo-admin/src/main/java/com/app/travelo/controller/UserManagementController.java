package com.app.travelo.controller;

import com.app.travelo.model.enums.UserStatus;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.RoleDto;
import com.app.travelo.model.rest.UserDto;
import com.app.travelo.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserManagementController {
    @Autowired
    private UserManagementService userService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createUser(@RequestBody UserDto req) {
        return new ResponseEntity<>(userService.saveInternalUser(req), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto<String>> updateUser(@RequestBody UserDto req) {
        return new ResponseEntity<>(userService.updateUser(req), HttpStatus.OK);
    }

    @GetMapping("/internal")
    public ResponseEntity<ResponseDto<List<UserDto>>> getInternalUsers() {
        return new ResponseEntity<>(userService.getInternalUsers(), HttpStatus.OK);
    }

    @GetMapping("/external")
    public ResponseEntity<ResponseDto<List<UserDto>>> getExternalUsers() {
        return new ResponseEntity<>(userService.getExternalUsers(), HttpStatus.OK);
    }

    @PostMapping("/inactivate")
    public ResponseEntity<ResponseDto<String>> inactivate(@RequestBody UserDto req) {
        return new ResponseEntity<>(userService.updateUserStatus(req, UserStatus.INACTIVE), HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseDto<String>> activate(@RequestBody UserDto req) {
        return new ResponseEntity<>(userService.updateUserStatus(req, UserStatus.ACTIVE), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResponseDto<List<RoleDto>>> getRoles() {
        return new ResponseEntity<>(userService.getRoles(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<String>> getUserDetail(@RequestBody UserDto req) {
        return new ResponseEntity<>(userService.updateUserStatus(req, UserStatus.ACTIVE), HttpStatus.OK);
    }
}


