package com.attend.service.Impl;

import com.alibaba.fastjson.JSONObject;

import com.attend.constant.MessageConstant;
import com.attend.dto.UserLoginDTO;
import com.attend.dto.UserRegisterDTO;
import com.attend.dto.UserUpdateDTO;
import com.attend.entity.User;
import com.attend.exception.LoginFailedException;
import com.attend.mapper.UserMapper;
import com.attend.properties.WeChatProperties;
import com.attend.service.UserService;
import com.attend.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    public User login(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            String avatar = userLoginDTO.getAvatar();
            String nickName = userLoginDTO.getNickName();
            user = User.builder()
                    .openid(openid)
                    .avatar(avatar)
                    .nickName(nickName)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    public User info(Long id) {
        User user = userMapper.getInfo(id);
        return user;
    }

    public void update(UserUpdateDTO userUpdateDTO) {
        userMapper.update(userUpdateDTO);
    }

    public String getById(Long id) {
        String name = userMapper.getById(id);
        return name;
    }

    public void update(UserRegisterDTO userRegisterDTO) {
        userMapper.updateById(userRegisterDTO);
    }

    //    public String getOpenid(String code) {
//        Map<String, String> map = new HashMap<>();
//        map.put("appid", weChatProperties.getAppid());
//        map.put("secret", weChatProperties.getSecret());
//        map.put("js_code", code);
//        map.put("grant_type", "authorization_code");
//        String json = HttpClientUtil.doGet(WX_LOGIN, map);
//        JSONObject jsonObject = JSON.parseObject(json);
//        String openid = jsonObject.getString("openid");
//        return openid;
//    }
    public String getOpenid(String code) {
        try {
            // 微信小程序登录凭证校验接口URL
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            // 准备参数
            Map<String, String> params = new HashMap<>();
            params.put("appid", weChatProperties.getAppid());
            params.put("secret", weChatProperties.getSecret());
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");

            // 发送 GET 请求
            String response = HttpClientUtil.doGet(url, params);
            JSONObject jsonObject = JSONObject.parseObject(response);
            // 检查错误码
            if (jsonObject.containsKey("errcode")) {
                // 处理错误情况
                int errcode = jsonObject.getInteger("errcode");
                String errmsg = jsonObject.getString("errmsg");
                throw new RuntimeException("Error from WeChat: " + errmsg + " (errcode: " + errcode + ")");
            }
            // 获取 openid
            return jsonObject.getString("openid");
        } catch (Exception e) {
            // 记录日志或进行其他异常处理
            // logger.error("获取openid失败", e);
            throw new RuntimeException("获取openid失败", e);
        }
    }
}
