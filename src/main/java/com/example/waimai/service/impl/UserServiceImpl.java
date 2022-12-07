package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.common.CustomException;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Setmeal;
import com.example.waimai.entity.User;
import com.example.waimai.mapper.CategoryMapper;
import com.example.waimai.mapper.UserMapper;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishService;
import com.example.waimai.service.SetmealService;
import com.example.waimai.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
