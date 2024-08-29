package com.attend.service;


import com.attend.dto.UserLoginDTO;
import com.attend.dto.UserRegisterDTO;
import com.attend.dto.UserUpdateDTO;
import com.attend.entity.User;

public interface UserService {
    //登录
    User login(UserLoginDTO userLoginDTO);
    //根据用户id查询用户信息
    User info(Long id);
    //更新用户信息
    void update(UserUpdateDTO userUpdateDTO);
    //查询姓名是否存在
    String getById(Long id);
    //接收注册信息
    void update(UserRegisterDTO userRegisterDTO);
}
