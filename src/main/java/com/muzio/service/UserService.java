package com.muzio.service;

import com.muzio.dto.UserDto;
import com.muzio.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
