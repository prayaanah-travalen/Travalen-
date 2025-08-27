package com.app.travelo.services;

import com.app.travelo.model.enums.UserStatus;
import com.app.travelo.model.rest.ResponseDto;
import com.app.travelo.model.rest.RoleDto;
import com.app.travelo.model.rest.UserDto;

import java.util.List;

public interface UserManagementService {

    ResponseDto<String> saveInternalUser(UserDto req);

    ResponseDto<List<UserDto>> getInternalUsers();

    ResponseDto<List<UserDto>> getExternalUsers();

    ResponseDto<String> updateUserStatus(UserDto req, UserStatus status);

    ResponseDto<List<RoleDto>> getRoles();

    ResponseDto<String> updateUser(UserDto req);

    ResponseDto<UserDto> getUserDetail(UserDto user);
}
