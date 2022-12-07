package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.Result;
import com.example.waimai.dto.UserDto;
import com.example.waimai.entity.AuthUtils;
import com.example.waimai.entity.User;
import com.example.waimai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.ClassNameMBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String authCode = AuthUtils.getCode(4);
            log.info("验证码: "+authCode);
            httpSession.setAttribute(phone, authCode);
            return Result.success(null, ""+user.getPhone()+" 发送验证码成功!");
        }else{
            return Result.success(null, ""+user.getPhone()+" 发送验证码失败!");
        }
    }

    /**
     * 可以用Map接收传参phone-> xxx, code-> xxx
     * @param userDto
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody UserDto userDto, HttpSession httpSession){
        String phone = userDto.getPhone();
        if(userDto.getCode()!=null && httpSession.getAttribute(phone).equals(userDto.getCode())){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if(user == null) { //未注册用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user", user.getId()); // 为了LoginCheckFilter里能记住登录
            return Result.success(user, " 登录成功");
        }else{
            return Result.error("登陆失败");
        }

    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return Result.success(null,"退出成功");
    }
}
