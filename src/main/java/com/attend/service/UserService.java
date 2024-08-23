package com.attend.service;


import com.attend.dto.UserLoginDTO;
import com.attend.entity.User;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
