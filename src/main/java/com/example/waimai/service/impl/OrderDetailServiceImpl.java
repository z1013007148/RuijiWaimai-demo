package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import com.example.waimai.entity.OrderDetail;
import com.example.waimai.mapper.DishMapper;
import com.example.waimai.mapper.OrderDetailMapper;
import com.example.waimai.service.DishFlavorService;
import com.example.waimai.service.DishService;
import com.example.waimai.service.OrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
