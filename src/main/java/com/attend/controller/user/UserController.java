package com.attend.controller.user;

import com.attend.constant.JwtClaimsConstant;
import com.attend.dto.UserLoginDTO;
import com.attend.dto.UserRegisterDTO;
import com.attend.dto.UserUpdateDTO;
import com.attend.entity.User;
import com.attend.properties.JwtProperties;
import com.attend.result.Result;
import com.attend.service.UserService;
import com.attend.utils.JwtUtil;
import com.attend.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("userController")
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "C端用户相关接口")
public class UserController {
    //微信登录
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        return Result.success();
    }
    //获取用户信息
    @GetMapping("/{id}")
    @ApiOperation("获取用户信息")
    public Result<User> info(@PathVariable long id) {
        log.info("获取用户信息:{}",id);
        User user = userService.info(id);
        return Result.success(user);
    }
    //更新用户信息
    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<String> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("更新用户信息:{}",userUpdateDTO);
        userService.update(userUpdateDTO);
        return Result.success();
    }
    //判断用户是否注册
    @GetMapping("/register/{id}")
    @ApiOperation("根据id查询姓名是否存在,判断用户是否注册")
    public Result<String> info(@PathVariable Long id) {
        log.info("根据id查询姓名是否存在,判断用户是否注册:{}",id);
       String name = userService.getById(id);
        return Result.success(name);
    }
    //接收注册信息
    @PutMapping("/register")
    @ApiOperation("接收注册信息")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("接收注册信息:{}",userRegisterDTO);
        userService.update(userRegisterDTO);
        return Result.success();
    }
}
