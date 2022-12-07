package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.entity.ShoppingCart;
import com.example.waimai.entity.User;
import com.example.waimai.mapper.ShoppingCartMapper;
import com.example.waimai.mapper.UserMapper;
import com.example.waimai.service.ShoppingCartService;
import com.example.waimai.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
